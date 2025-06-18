package com.m7md7sn.dentel.presentation.ui.favorites.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.repository.FavoriteType
import com.m7md7sn.dentel.presentation.theme.DentelBrightBlue
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple
import com.m7md7sn.dentel.presentation.ui.profile.components.FavoriteButton

/**
 * Component that displays toggle buttons for favorite types (Articles/Videos)
 */
@Composable
fun FavoriteTypeSelector(
    selectedType: FavoriteType,
    onTypeSelected: (FavoriteType) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLanguage = LocalConfiguration.current.locale.language

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Article button
        FavoriteButton(
            onClick = { onTypeSelected(FavoriteType.ARTICLE) },
            clicked = selectedType == FavoriteType.ARTICLE,
            iconRes = R.drawable.ic_fav_articles,
            clickedIconRes = R.drawable.ic_fav_articles_selected,
            text = R.string.fav_articles,
            highlightedTextColor = DentelLightPurple,
            color = DentelLightPurple
        )

        Spacer(modifier = Modifier.width(24.dp))

        // Video button
        FavoriteButton(
            onClick = { onTypeSelected(FavoriteType.VIDEO) },
            clicked = selectedType == FavoriteType.VIDEO,
            iconRes = R.drawable.ic_fav_videos,
            clickedIconRes = R.drawable.ic_fav_videos_selected,
            text = R.string.fav_videos,
            highlightedTextColor = DentelBrightBlue,
            color = DentelBrightBlue
        )
    }
}
