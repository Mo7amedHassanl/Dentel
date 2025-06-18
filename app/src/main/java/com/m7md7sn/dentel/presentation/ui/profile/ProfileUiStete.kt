package com.m7md7sn.dentel.presentation.ui.profile

import com.m7md7sn.dentel.data.model.User
import com.m7md7sn.dentel.data.repository.FavoriteItem
import com.m7md7sn.dentel.data.repository.FavoriteType

/**
 * Sealed interface representing different states of the profile UI
 */
sealed interface ProfileUiState {
    /**
     * Initial loading state when data is being fetched
     */
    data object Loading : ProfileUiState

    /**
     * Success state when data is loaded successfully
     */
    data class Success(
        val user: User,
        val favoriteItems: List<FavoriteItem> = emptyList(),
        val selectedFavoriteType: FavoriteType = FavoriteType.ARTICLE,
        val isLoadingFavorites: Boolean = false
    ) : ProfileUiState

    /**
     * Error state when something goes wrong
     */
    data class Error(val message: String) : ProfileUiState
}
