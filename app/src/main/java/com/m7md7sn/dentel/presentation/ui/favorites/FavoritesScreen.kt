package com.m7md7sn.dentel.presentation.ui.favorites

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.profile.components.FavoriteItem


@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FavoritesHeader()
            FullFavoritesList(
                Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun FullFavoritesList(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 42.dp),
    ) {
        items(10) {
            FavoriteItem(
                title = "Favorite Item ${it + 1}",
                onCardClicked = { /* Handle click */ },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun FavoritesHeader(
    modifier: Modifier = Modifier
) {
    val currentLanguage = LocalConfiguration.current.locale.language

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
                        .height(150.dp)
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
                .align(Alignment.Center),
            iconRes = R.drawable.ic_fav_articles_white,
            titleRes = R.string.fav_articles_title,
            highlightedText = if(currentLanguage == "ar") "المقالات" else "Articles",
            highlightedTextColor = DentelLightPurple
        )
    }
}

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
    val endIndex = startIndex + highlightedText.length

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
                    withStyle(style = SpanStyle(color = White)) {
                        append(fullString.substring(0, startIndex))
                    }
                    withStyle(style = SpanStyle(color = highlightedTextColor)) {
                        append(fullString.substring(startIndex, endIndex))
                    }
                    withStyle(style = SpanStyle(color = White)) {
                        append(fullString.substring(endIndex))
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

@Preview
@Composable
private fun FavoritesScreenPreviewEn() {
    DentelTheme {
        FavoritesScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun FavoritesScreenPreviewAr() {
    DentelTheme {
        FavoritesScreen()
    }
}