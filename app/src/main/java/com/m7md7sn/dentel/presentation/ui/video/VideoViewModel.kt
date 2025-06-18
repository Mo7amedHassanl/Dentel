package com.m7md7sn.dentel.presentation.ui.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.model.Video
import com.m7md7sn.dentel.data.repository.VideoRepository
import com.m7md7sn.dentel.data.repository.FavoritesRepository
import com.m7md7sn.dentel.data.model.FavoriteItem
import com.m7md7sn.dentel.data.repository.FavoriteType
import com.m7md7sn.dentel.presentation.ui.section.Topic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(VideoUiState())
    val uiState: StateFlow<VideoUiState> = _uiState
    private var currentFavoriteItem: FavoriteItem? = null

    fun loadVideo(videoId: String) {
        viewModelScope.launch {
            videoRepository.getVideoDetails(videoId).collect { video ->
                _uiState.update { it.copy(
                    video = video,
                    isLoading = false
                ) }
                video?.let { observeFavorite(it) }
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
                video?.let { observeFavorite(it) }
            }
        }
    }

    private fun observeFavorite(video: com.m7md7sn.dentel.data.model.Video) {
        val favItem = FavoriteItem(
            id = video.id,
            title = video.title,
            type = FavoriteType.VIDEO
        )
        currentFavoriteItem = favItem
        viewModelScope.launch {
            favoritesRepository.getFavoriteVideos().collectLatest { favs ->
                _uiState.update { it.copy(isFavorite = favs.any { it.id == favItem.id }) }
            }
        }
    }

    fun toggleFavorite() {
        val favItem = currentFavoriteItem ?: return
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(favItem)
            favoritesRepository.getFavoriteVideos().collectLatest { favs ->
                _uiState.update { it.copy(isFavorite = favs.any { it.id == favItem.id }) }
                return@collectLatest
            }
        }
    }
}
