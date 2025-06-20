package com.m7md7sn.dentel.presentation.ui.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.model.SuggestedTopic
import com.m7md7sn.dentel.data.model.Video
import com.m7md7sn.dentel.data.repository.VideoRepository
import com.m7md7sn.dentel.data.repository.FavoritesRepository
import com.m7md7sn.dentel.data.model.FavoriteItem
import com.m7md7sn.dentel.data.repository.FavoriteType
import com.m7md7sn.dentel.data.repository.HomeRepository
import com.m7md7sn.dentel.data.repository.SectionRepository
import com.m7md7sn.dentel.presentation.ui.section.Topic
import com.m7md7sn.dentel.presentation.ui.section.TopicType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val favoritesRepository: FavoritesRepository,
    private val homeRepository: HomeRepository,
    private val sectionRepository: SectionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(VideoUiState())
    val uiState: StateFlow<VideoUiState> = _uiState
    private var currentFavoriteItem: FavoriteItem? = null

    // Navigation state for handling suggested topic clicks
    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.Idle)
    val navigationState: StateFlow<NavigationState> = _navigationState

    init {
        loadSuggestedTopics()
    }

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

    private fun loadSuggestedTopics() {
        viewModelScope.launch {
            homeRepository.getSuggestedTopics()
                .catch { error ->
                    _uiState.update { it.copy(
                        suggestedTopicsError = "Failed to load suggested topics: ${error.message}"
                    ) }
                }
                .collect { topics ->
                    _uiState.update { it.copy(
                        suggestedTopics = topics
                    ) }
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

    /**
     * Handle click on a suggested topic
     */
    fun onSuggestedTopicClicked(suggestedTopic: SuggestedTopic) {
        viewModelScope.launch {
            // Update navigation state to loading
            _navigationState.update { NavigationState.Loading }

            try {
                // Convert the string type to TopicType enum
                val topicType = when (suggestedTopic.type.lowercase()) {
                    "video" -> TopicType.Video
                    "article" -> TopicType.Article
                    else -> {
                        // If type is not specified clearly, try to determine from the id format or default to Article
                        if (suggestedTopic.id.contains("video", ignoreCase = true)) {
                            TopicType.Video
                        } else {
                            TopicType.Article
                        }
                    }
                }

                // Get topic details from the repository
                val topic = sectionRepository.getTopicById(suggestedTopic.id, topicType)

                if (topic != null) {
                    // Update navigation state with the topic
                    _navigationState.update { NavigationState.NavigateToContent(topic) }
                } else {
                    _navigationState.update { NavigationState.Error("Could not find topic with ID: ${suggestedTopic.id}") }
                }
            } catch (e: Exception) {
                _navigationState.update { NavigationState.Error("Error loading topic: ${e.message}") }
            }
        }
    }

    /**
     * Reset navigation state after navigation is handled
     */
    fun resetNavigationState() {
        _navigationState.update { NavigationState.Idle }
    }
}

/**
 * Navigation state for the suggested topic navigation
 */
sealed class NavigationState {
    data object Idle : NavigationState()
    data object Loading : NavigationState()
    data class NavigateToContent(val topic: Topic) : NavigationState()
    data class Error(val message: String) : NavigationState()
}
