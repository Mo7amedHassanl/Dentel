package com.m7md7sn.dentel.presentation.ui.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.m7md7sn.dentel.data.model.FavoriteItem
import com.m7md7sn.dentel.data.repository.FavoriteType
import com.m7md7sn.dentel.presentation.theme.DentelBrightBlue
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple

/**
 * Section that displays the favorite selection buttons (Articles/Videos)
 */
@Composable
fun FavoritesButtons(
    selectedType: FavoriteType,
    onFavoriteTypeSelected: (FavoriteType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        // Favorites Videos button
        FavoriteButton(
            onClick = { onFavoriteTypeSelected(FavoriteType.VIDEO) },
            clicked = selectedType == FavoriteType.VIDEO,
            iconRes = R.drawable.ic_fav_videos,
            clickedIconRes = R.drawable.ic_fav_videos_selected,
            text = R.string.fav_videos,
            highlightedTextColor = DentelBrightBlue,
            color = DentelBrightBlue,
        )
        Spacer(modifier = Modifier.width(24.dp))

        // Favorites Articles button
        FavoriteButton(
            onClick = { onFavoriteTypeSelected(FavoriteType.ARTICLE) },
            clicked = selectedType == FavoriteType.ARTICLE,
            iconRes = R.drawable.ic_fav_articles,
            clickedIconRes = R.drawable.ic_fav_articles_selected,
            text = R.string.fav_articles,
            highlightedTextColor = DentelLightPurple,
            color = DentelLightPurple,
        )
    }
}

/**
 * List of favorite items
 */
@Composable
fun FavoritesList(
    items: List<FavoriteItem>,
    isLoading: Boolean,
    onItemClick: (FavoriteItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 42.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color(0xFF421882),
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (items.isEmpty()) {
            Text(
                text = "No favorites yet",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn {
                items(items.size) { index ->
                    val item = items[index]
                    FavoriteItem(
                        title = item.title,
                        onCardClicked = { onItemClick(item) },
                        modifier = Modifier.padding(vertical = 8.dp),
                        type = item.type
                    )
                }
            }
        }
    }
}

/**
 * Single favorite item card
 */
@Composable
fun FavoriteItem(
    title: String,
    type: FavoriteType,
    onCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(307.dp)
            .height(83.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (type == FavoriteType.ARTICLE) Color(0xFFE5E1FF) else Color(0xFFE1F0FF),
        ),
        shape = RoundedCornerShape(20.dp),
        onClick = onCardClicked,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.5.dp
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 25.sp,
                    lineHeight = 30.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Center,
                )
            )
        }
    }
}
