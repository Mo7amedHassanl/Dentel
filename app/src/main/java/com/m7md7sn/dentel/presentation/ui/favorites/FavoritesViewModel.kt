package com.m7md7sn.dentel.presentation.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.model.FavoriteItem
import com.m7md7sn.dentel.data.repository.FavoriteType
import com.m7md7sn.dentel.data.repository.FavoritesRepository
import com.m7md7sn.dentel.data.repository.SectionRepository
import com.m7md7sn.dentel.presentation.ui.section.TopicType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Favorites screen that handles UI state and user interactions
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val sectionRepository: SectionRepository
) : ViewModel() {

    // Private mutable state flow that can only be modified within the ViewModel
    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)

    // Public immutable state flow that can be observed by the UI
    val uiState: StateFlow<FavoritesUiState> = _uiState

    // Navigation state for handling navigation to content screens
    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.Idle)
    val navigationState: StateFlow<NavigationState> = _navigationState

    // Currently selected favorite type (Articles or Videos)
    private var currentType = FavoriteType.ARTICLE

    init {
        loadFavorites(currentType)
    }

    /**
     * Load favorites based on the selected type
     */
    fun loadFavorites(type: FavoriteType) {
        viewModelScope.launch {
            _uiState.value = FavoritesUiState.Loading
            currentType = type

            val favoritesFlow = when (type) {
                FavoriteType.ARTICLE -> favoritesRepository.getFavoriteArticles()
                FavoriteType.VIDEO -> favoritesRepository.getFavoriteVideos()
            }

            favoritesFlow
                .catch { error ->
                    _uiState.value = FavoritesUiState.Error(
                        message = error.message ?: "Unknown error occurred",
                        selectedType = type
                    )
                }
                .collect { favorites ->
                    if (favorites.isEmpty()) {
                        _uiState.value = FavoritesUiState.Empty(selectedType = type)
                    } else {
                        _uiState.value = FavoritesUiState.Success(
                            favorites = favorites,
                            selectedType = type
                        )
                    }
                }
        }
    }

    /**
     * Toggle the favorite type (Articles/Videos)
     */
    fun toggleFavoriteType() {
        val newType = when (currentType) {
            FavoriteType.ARTICLE -> FavoriteType.VIDEO
            FavoriteType.VIDEO -> FavoriteType.ARTICLE
        }

        loadFavorites(newType)
    }

    /**
     * Remove an item from favorites
     */
    fun removeFavorite(item: FavoriteItem) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(item)
            // Refresh the list after removing
            loadFavorites(currentType)
        }
    }

    /**
     * Refresh favorites data
     */
    fun refreshFavorites() {
        // Set isRefreshing flag if we're already in Success state
        if (_uiState.value is FavoritesUiState.Success) {
            _uiState.value = (_uiState.value as FavoritesUiState.Success).copy(
                isRefreshing = true
            )
        }

        loadFavorites(currentType)
    }

    /**
     * Handle favorite item click and fetch the corresponding topic
     */
    fun onFavoriteItemClicked(favoriteItem: FavoriteItem) {
        _navigationState.value = NavigationState.Loading

        viewModelScope.launch {
            try {
                // Convert FavoriteType to TopicType
                val topicType = when (favoriteItem.type) {
                    FavoriteType.ARTICLE -> TopicType.Article
                    FavoriteType.VIDEO -> TopicType.Video
                    else -> null // Handle any potential future types
                }

                if (topicType != null) {
                    // Use the optimized method that takes a topic type parameter
                    val topic = sectionRepository.getTopicById(favoriteItem.id, topicType)
                    if (topic != null) {
                        _navigationState.value = NavigationState.NavigateToContent(topic)
                    } else {
                        _navigationState.value = NavigationState.Error("Content not found")
                    }
                } else {
                    _navigationState.value = NavigationState.Error("Unknown favorite type")
                }
            } catch (e: Exception) {
                _navigationState.value = NavigationState.Error(e.message ?: "Error loading content")
            }
        }
    }

    /**
     * Reset navigation state after navigation is handled
     */
    fun resetNavigationState() {
        _navigationState.value = NavigationState.Idle
    }
}

/**
 * Represents the various states of navigation
 */
sealed class NavigationState {
    object Idle : NavigationState()
    object Loading : NavigationState()
    data class NavigateToContent(val topic: com.m7md7sn.dentel.presentation.ui.section.Topic) : NavigationState()
    data class Error(val message: String) : NavigationState()
}