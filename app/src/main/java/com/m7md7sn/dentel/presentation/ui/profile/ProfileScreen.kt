package com.m7md7sn.dentel.presentation.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentel.data.model.User
import com.m7md7sn.dentel.data.model.FavoriteItem
import com.m7md7sn.dentel.data.repository.FavoriteType
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.profile.components.FavoritesList
import com.m7md7sn.dentel.presentation.ui.profile.components.FavoritesButtons
import com.m7md7sn.dentel.presentation.ui.profile.components.ProfileHeader
import com.m7md7sn.dentel.presentation.ui.profile.components.ShowMoreButton
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.runtime.setValue

/**
 * Main Profile Screen that displays user information and favorites
 */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onFavoriteItemClick: (FavoriteItem) -> Unit = {},
    onNavigateToFavorites: (Int) -> Unit = {} // Updated to pass the type ordinal
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        when (uiState) {
            is ProfileUiState.Loading -> {
                // Display loading state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF421882))
                }
            }

            is ProfileUiState.Error -> {
                // Display error state
                val errorState = uiState as ProfileUiState.Error
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${errorState.message}",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            is ProfileUiState.Success -> {
                // Display success state with profile data
                val successState = uiState as ProfileUiState.Success
                ProfileContent(
                    user = successState.user,
                    favoriteItems = successState.favoriteItems,
                    selectedFavoriteType = successState.selectedFavoriteType,
                    isLoadingFavorites = successState.isLoadingFavorites,
                    onFavoriteTypeSelected = viewModel::loadFavorites,
                    onFavoriteItemClick = onFavoriteItemClick,
                    onShowMoreClick = {
                        // Pass the type ordinal when navigating to favorites
                        onNavigateToFavorites(successState.selectedFavoriteType.ordinal)
                    }
                )
            }
        }
    }
}

/**
 * Main content of the Profile screen when data is loaded successfully
 */
@Composable
private fun ProfileContent(
    user: User,
    favoriteItems: List<FavoriteItem>,
    selectedFavoriteType: FavoriteType,
    isLoadingFavorites: Boolean,
    onFavoriteTypeSelected: (FavoriteType) -> Unit,
    onFavoriteItemClick: (FavoriteItem) -> Unit,
    onShowMoreClick: () -> Unit
) {
    // Remember previous type to determine animation direction
    var previousType by rememberSaveable { mutableStateOf(selectedFavoriteType) }
    val slideDirection = when {
        previousType == selectedFavoriteType -> 0
        selectedFavoriteType == FavoriteType.VIDEO -> -1 // Slide left for Article -> Video
        else -> 1 // Slide right for Video -> Article
    }
    LaunchedEffect(selectedFavoriteType) { previousType = selectedFavoriteType }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile header with user information
        ProfileHeader(user = user)

        // Favorites type selection buttons (Articles/Videos)
        FavoritesButtons(
            selectedType = selectedFavoriteType,
            onFavoriteTypeSelected = onFavoriteTypeSelected
        )

        // Animated list of favorite items, animation only on type change
        AnimatedContent(
            targetState = selectedFavoriteType,
            transitionSpec = {
                (slideInHorizontally(animationSpec = tween(400)) { width -> slideDirection * width } +
                        fadeIn(animationSpec = tween(400))) togetherWith
                        (slideOutHorizontally(animationSpec = tween(400)) { width -> -slideDirection * width } +
                                fadeOut(animationSpec = tween(300)))
            },
            label = "favorites_content_animation"
        ) { _ ->
            FavoritesList(
                items = favoriteItems,
                isLoading = isLoadingFavorites,
                onItemClick = onFavoriteItemClick,
                modifier = Modifier.weight(1f)
            )
        }

        // Show more button at the bottom
        ShowMoreButton(onClick = onShowMoreClick)
    }
}

@Preview
@Composable
private fun ProfileScreenPreviewEn() {
    DentelTheme {
        ProfileScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun ProfileScreenPreviewAr() {
    DentelTheme {
        ProfileScreen()
    }
}