package com.amolina.handspeak.ui.gestures

import androidx.lifecycle.ViewModel
import com.amolina.handspeak.ui.gestures.helpers.ImageAnalyzerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HandGestureUiState(
    val mostRecentGesture: String? = null,
)

@HiltViewModel
class HandGestureViewModel @Inject constructor(
    val imageAnalyzerHelper: ImageAnalyzerHelper
) : ViewModel() {


    private val _uiState = MutableStateFlow(HandGestureUiState())
    val uiState: StateFlow<HandGestureUiState> = _uiState.asStateFlow()

    init {
        imageAnalyzerHelper.gestureRecognizerHelper.setOnGestureRecognizedListener { gesture ->
            updateGesture(gesture)
        }
    }

    private fun updateGesture(gesture: String) {
        _uiState.value = HandGestureUiState(gesture)
    }
}