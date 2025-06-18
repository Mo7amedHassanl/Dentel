package com.m7md7sn.dentel.presentation.ui.favorites.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.repository.FavoriteType
import com.m7md7sn.dentel.presentation.theme.DentelBrightBlue
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple

data class FavoriteHeaderData(
    val iconRes: Int,
    val titleRes: Int,
    val highlightedText: String,
    val highlightedTextColor: Color
)

/**
 * Header for the Favorites screen
 */
@Composable
fun FavoritesHeader(
    languageCode: String,
    selectedType: FavoriteType,
    modifier: Modifier = Modifier,
) {
    // Create a local data class to support destructuring
    val (iconRes, titleRes, highlightedText, highlightedTextColor) = when (selectedType) {
        FavoriteType.ARTICLE -> FavoriteHeaderData(
            R.drawable.ic_fav_articles_white,
            R.string.fav_articles_title,
            if (languageCode == "ar") "المقالات" else "Articles",
            DentelLightPurple
        )

        FavoriteType.VIDEO -> FavoriteHeaderData(
            R.drawable.ic_fav_videos_white,
            R.string.fav_videos_title,
            if (languageCode == "ar") "الفيديوهات" else "Videos",
            DentelBrightBlue
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            color = DentelDarkPurple,
                            shape = RoundedCornerShape(
                                bottomStart = 50.dp,
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.favorites_tooth_bg),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .alpha(0.3f)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = DentelDarkPurple,
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topEnd = 40.dp)
                        )
                        .align(Alignment.End)
                )
            }
        }
        FavoritesTitle(
            modifier = Modifier
                .align(Alignment.TopCenter),
            iconRes = iconRes,
            titleRes = titleRes,
            highlightedText = highlightedText,
            highlightedTextColor = highlightedTextColor
        )
    }
}

/**
 * Title component for the favorites screen
 */
@Composable
fun FavoritesTitle(
    titleRes: Int,
    highlightedText: String,
    highlightedTextColor: Color,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier
) {
    val fullString = stringResource(titleRes)

    val startIndex = fullString.indexOf(highlightedText)
    val endIndex = if (startIndex != -1) startIndex + highlightedText.length else -1

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(42.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = buildAnnotatedString {
                if (startIndex != -1) {
                    withStyle(style = SpanStyle(color = Color.White)) {
                        append(fullString.substring(0, startIndex))
                    }
                    withStyle(style = SpanStyle(color = highlightedTextColor)) {
                        append(fullString.substring(startIndex, endIndex))
                    }
                    if (endIndex < fullString.length) {
                        withStyle(style = SpanStyle(color = Color.White)) {
                            append(fullString.substring(endIndex))
                        }
                    }
                } else {
                    append(fullString)
                }
            },
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 21.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                fontWeight = FontWeight(700),
                color = Color(0xFF05B3EF),
                textAlign = TextAlign.Center,
                letterSpacing = 0.4.sp,
            )
        )
    }
}
