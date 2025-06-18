package com.m7md7sn.dentel.presentation.ui.favorites

import com.m7md7sn.dentel.data.repository.FavoriteItem
import com.m7md7sn.dentel.data.repository.FavoriteType

/**
 * Sealed interface representing different states of the favorites screen UI
 */
sealed interface FavoritesUiState {
    /**
     * Initial loading state
     */
    data object Loading : FavoritesUiState

    /**
     * Success state with favorites data
     */
    data class Success(
        val favorites: List<FavoriteItem> = emptyList(),
        val selectedType: FavoriteType = FavoriteType.ARTICLE,
        val isRefreshing: Boolean = false
    ) : FavoritesUiState

    /**
     * Empty state when no favorites are available
     */
    data class Empty(
        val selectedType: FavoriteType = FavoriteType.ARTICLE
    ) : FavoritesUiState

    /**
     * Error state when data loading fails
     */
    data class Error(
        val message: String,
        val selectedType: FavoriteType = FavoriteType.ARTICLE
    ) : FavoritesUiState
}
