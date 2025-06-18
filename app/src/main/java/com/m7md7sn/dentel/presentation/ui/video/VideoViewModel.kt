package com.m7md7sn.dentel.presentation.ui.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.model.Video
import com.m7md7sn.dentel.data.repository.VideoRepository
import com.m7md7sn.dentel.presentation.ui.section.Topic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoRepository: VideoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(VideoUiState())
    val uiState: StateFlow<VideoUiState> = _uiState

    fun loadVideo(videoId: String) {
        viewModelScope.launch {
            videoRepository.getVideoDetails(videoId).collect { video ->
                _uiState.update { it.copy(
                    video = video,
                    isLoading = false
                ) }
            }
        }
    }

    fun loadVideoFromTopic(topic: Topic) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            videoRepository.getVideoFromTopic(topic).collect { video ->
                _uiState.update { it.copy(
                    video = video,
                    isLoading = false,
                    errorMessage = if (video == null) "Unable to load video" else null
                ) }
            }
        }
    }
}
