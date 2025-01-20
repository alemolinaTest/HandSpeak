package com.amolina.handspeak.ui.gestures.helpers

import android.content.Context
import android.graphics.Bitmap
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizer
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizerResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GestureRecognizerHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var onGestureRecognized: ((String) -> Unit)? = null

    private val gestureRecognizer: GestureRecognizer by lazy {
        val baseOptions = BaseOptions.builder().setModelAssetPath("gesture_recognizer.task").build()
        val options = GestureRecognizer.GestureRecognizerOptions.builder()
            .setBaseOptions(baseOptions)
            .setResultListener { result, _ -> handleGestureRecognizerResult(result) }
            .setRunningMode(RunningMode.LIVE_STREAM)
            .build()

        GestureRecognizer.createFromOptions(context, options)
    }

    fun setOnGestureRecognizedListener(listener: (String) -> Unit) {
        onGestureRecognized = listener
    }

    fun recognize(bitmap: Bitmap, timestamp: Long) {
        gestureRecognizer.recognizeAsync(
            com.google.mediapipe.framework.image.BitmapImageBuilder(bitmap).build(),
            timestamp
        )
    }

    private fun handleGestureRecognizerResult(result: GestureRecognizerResult) {
        val bestCategory = result.gestures().firstOrNull()?.maxByOrNull { it.score() }
        val gesture = when (bestCategory?.categoryName()) {
            "Thumb_Up" -> "\uD83D\uDC4D"
            "Thumb_Down" -> "\uD83D\uDC4E"
            "Pointing_Up" -> "☝\uFE0F"
            "Open_Palm" -> "✋"
            "Closed_Fist" -> "✊"
            "Victory" -> "✌\uFE0F"
            "ILoveYou" -> "\uD83E\uDD1F"
            else -> "😡"
        }

        gesture?.let { onGestureRecognized?.invoke(it) }
    }
}