package com.m7md7sn.dentel.presentation.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentel.data.repository.FavoriteItem
import com.m7md7sn.dentel.data.repository.FavoriteType
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.favorites.components.EmptyFavoritesMessage
import com.m7md7sn.dentel.presentation.ui.favorites.components.ErrorMessage
import com.m7md7sn.dentel.presentation.ui.favorites.components.FavoritesList
import com.m7md7sn.dentel.presentation.ui.favorites.components.FavoritesHeader

/**
 * Main screen for displaying user's favorite content
 */
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel(),
    onFavoriteClick: (FavoriteItem) -> Unit = {},
    selectedTypeOrdinal: Int
) {
    // Initialize with the passed type
    val selectedType = FavoriteType.values()[selectedTypeOrdinal]

    // Load the correct type of favorites
    LaunchedEffect(selectedType) {
        viewModel.loadFavorites(selectedType)
    }

    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        // Extract state-specific properties
        val currentType = when (uiState) {
            is FavoritesUiState.Success -> (uiState as FavoritesUiState.Success).selectedType
            is FavoritesUiState.Empty -> (uiState as FavoritesUiState.Empty).selectedType
            is FavoritesUiState.Error -> (uiState as FavoritesUiState.Error).selectedType
            else -> selectedType // Default for Loading state
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FavoritesHeader(
                selectedType = selectedType,
                languageCode = LocalConfiguration.current.locale.language,
            )
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (uiState) {
                    is FavoritesUiState.Loading -> {
                        CircularProgressIndicator(color = Color(0xFF421882))
                    }
                    is FavoritesUiState.Success -> {
                        val successState = uiState as FavoritesUiState.Success
                        FavoritesList(
                            favorites = successState.favorites,
                            isLoading = successState.isRefreshing,
                            onFavoriteClick = onFavoriteClick
                        )
                    }
                    is FavoritesUiState.Empty -> {
                        EmptyFavoritesMessage(selectedType = currentType)
                    }
                    is FavoritesUiState.Error -> {
                        ErrorMessage(message = (uiState as FavoritesUiState.Error).message)
                    }
                }
            }
        }
    }
}
