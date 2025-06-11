package com.m7md7sn.dentel.presentation.ui.video

import androidx.lifecycle.ViewModel
import com.m7md7sn.dentel.data.model.Video
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class VideoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(VideoUiState())
    val uiState: StateFlow<VideoUiState> = _uiState

    fun setVideo(video: Video) {
        _uiState.update { it.copy(video = video) }
    }
} 