package com.amolina.handspeak.ui.camera

import androidx.camera.core.ImageAnalysis
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    imageAnalysisUseCase: ImageAnalysis?,
    viewModel: CameraViewModel = viewModel()
) {
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewUseCase by viewModel.previewUseCase.collectAsState()
    val cameraProvider by viewModel.cameraProviderFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initializeCameraProvider(localContext)
    }

    LaunchedEffect(cameraProvider) {
        if (cameraProvider != null) {
            viewModel.rebindCameraProvider(lifecycleOwner, imageAnalysisUseCase)
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            PreviewView(context).also {
                previewUseCase?.surfaceProvider = it.surfaceProvider
                viewModel.rebindCameraProvider(lifecycleOwner, imageAnalysisUseCase)
            }
        }
    )
}