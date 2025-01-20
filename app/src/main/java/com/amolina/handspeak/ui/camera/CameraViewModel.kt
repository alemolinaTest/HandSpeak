package com.amolina.handspeak.ui.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {

    private val _cameraProviderFlow = MutableStateFlow<ProcessCameraProvider?>(null)
    val cameraProviderFlow: StateFlow<ProcessCameraProvider?> = _cameraProviderFlow

    private val _previewUseCase = MutableStateFlow<Preview?>(null)
    val previewUseCase: StateFlow<Preview?> = _previewUseCase

    init {
        _previewUseCase.value = Preview.Builder().build()
    }

    fun initializeCameraProvider(context: Context) {
        viewModelScope.launch {
            val provider = ProcessCameraProvider.awaitInstance(context)
            _cameraProviderFlow.value = provider
        }
    }

    fun rebindCameraProvider(
        lifecycleOwner: LifecycleOwner,
        imageAnalysisUseCase: ImageAnalysis?
    ) {
        _cameraProviderFlow.value?.let { cameraProvider ->
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                _previewUseCase.value, imageAnalysisUseCase
            )
        }
    }
}