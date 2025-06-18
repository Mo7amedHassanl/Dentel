package com.m7md7sn.dentel.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.repository.FavoriteType
import com.m7md7sn.dentel.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Profile screen, responsible for managing UI state
 * and coordinating between the UI and data layer
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    // Private mutable state flow that can only be modified within the ViewModel
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)

    // Public immutable state flow that can be observed by the UI
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadUserProfile()
    }

    /**
     * Load user profile information from the repository
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            profileRepository.getUserProfile()
                .catch { e ->
                    _uiState.value = ProfileUiState.Error(e.message ?: "Unknown error occurred")
                }
                .collect { user ->
                    _uiState.value = ProfileUiState.Success(user = user)
                    // After setting the initial success state, load favorites
                    loadFavorites(FavoriteType.ARTICLE)
                }
        }
    }

    /**
     * Load favorite items of a specific type
     */
    fun loadFavorites(favoriteType: FavoriteType) {
        val currentState = _uiState.value

        // Only proceed if we're in a success state
        if (currentState is ProfileUiState.Success) {
            // Mark that we're loading favorites
            _uiState.value = currentState.copy(
                selectedFavoriteType = favoriteType,
                isLoadingFavorites = true
            )

            viewModelScope.launch {
                profileRepository.getFavoriteItems(favoriteType)
                    .catch { e ->
                        val errorState = _uiState.value
                        if (errorState is ProfileUiState.Success) {
                            _uiState.value = errorState.copy(isLoadingFavorites = false)
                        }
                    }
                    .collect { favorites ->
                        val successState = _uiState.value
                        if (successState is ProfileUiState.Success) {
                            _uiState.value = successState.copy(
                                favoriteItems = favorites,
                                isLoadingFavorites = false
                            )
                        }
                    }
            }
        }
    }

    /**
     * Toggle between favorite types (Article/Video)
     */
    fun toggleFavoriteType() {
        val currentState = _uiState.value
        if (currentState is ProfileUiState.Success) {
            val newType = when (currentState.selectedFavoriteType) {
                FavoriteType.ARTICLE -> FavoriteType.VIDEO
                FavoriteType.VIDEO -> FavoriteType.ARTICLE
            }
            loadFavorites(newType)
        }
    }
}