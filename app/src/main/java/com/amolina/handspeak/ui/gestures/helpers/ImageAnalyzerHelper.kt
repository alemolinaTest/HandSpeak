package com.amolina.handspeak.ui.gestures.helpers

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import javax.inject.Inject
import kotlin.math.max

class ImageAnalyzerHelper @Inject constructor(
    val gestureRecognizerHelper: GestureRecognizerHelper
) : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {
        val imageBitmap = image.toBitmap()
        val scale = 500f / max(image.width, image.height)

        val scaleAndRotate = Matrix().apply {
            postScale(scale, scale)
            postRotate(image.imageInfo.rotationDegrees.toFloat())
        }

        val scaledAndRotatedBmp = Bitmap.createBitmap(
            imageBitmap, 0, 0, image.width, image.height, scaleAndRotate, true
        )

        image.close()

        gestureRecognizerHelper.recognize(scaledAndRotatedBmp, System.currentTimeMillis())
    }
}