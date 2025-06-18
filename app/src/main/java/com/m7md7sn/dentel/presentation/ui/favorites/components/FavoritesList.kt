package com.m7md7sn.dentel.presentation.ui.favorites.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.repository.FavoriteItem
import com.m7md7sn.dentel.data.repository.FavoriteType
import com.m7md7sn.dentel.presentation.ui.profile.components.FavoriteItem as ProfileFavoriteItem

/**
 * Component that displays the list of favorite items
 */
@Composable
fun FavoritesList(
    favorites: List<FavoriteItem>,
    isLoading: Boolean,
    onFavoriteClick: (FavoriteItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 42.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color(0xFF421882),
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (favorites.isEmpty()) {
            EmptyFavoritesMessage(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(favorites.size) { index ->
                    val item = favorites[index]
                    ProfileFavoriteItem(
                        title = item.title,
                        onCardClicked = { onFavoriteClick(item) },
                        modifier = Modifier.padding(vertical = 8.dp),
                        type = item.type,
                    )
                }
            }
        }
    }
}

/**
 * Message displayed when no favorites are available
 */
@Composable
fun EmptyFavoritesMessage(
    modifier: Modifier = Modifier,
    selectedType: FavoriteType = FavoriteType.ARTICLE
) {
    val message = when (selectedType) {
        FavoriteType.ARTICLE -> "You don't have any favorite articles yet"
        FavoriteType.VIDEO -> "You don't have any favorite videos yet"
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xFF421882),
                textAlign = TextAlign.Center,
            )
        )
    }
}

/**
 * Message displayed when error occurs
 */
@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                fontWeight = FontWeight(400),
                color = Color.Red,
                textAlign = TextAlign.Center,
            )
        )
    }
}
