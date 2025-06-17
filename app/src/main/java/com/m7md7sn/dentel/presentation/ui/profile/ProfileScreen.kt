package com.m7md7sn.dentel.presentation.ui.profile

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelBrightBlue
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.home.SuggestedTopicItem


@Composable
fun ProfileScreen(modifier: Modifier = Modifier, viewModel: ProfileViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

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
            ProfileHeader(uiState = uiState)
            Spacer(Modifier.height(16.dp))
            FavoritesButtons()
            Spacer(Modifier.height(12.dp))
            FavoritesList()
            Spacer(
                Modifier.weight(1f)
            )
            ShowMoreButton()
        }
    }
}

@Composable
fun ShowMoreButton(
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier
            .width(68.dp)
            .height(28.dp),
        onClick = { /* Handle click */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = DentelBrightBlue,
            contentColor = White
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
fun FavoritesList(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 42.dp),
    ) {
        items(3) {
            FavoriteItem(
                title = "Favorite Item ${it + 1}",
                onCardClicked = { /* Handle click */ },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun FavoriteItem(
    title: String,
    onCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(307.dp)
            .height(83.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE5E1FF),
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

@Composable
fun FavoritesButtons(
    modifier: Modifier = Modifier
) {
    val currentLanguage = LocalConfiguration.current.locale.language

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        //Fav Videos
        FavoriteButton(
            onClick = { /* TODO */ },
            clicked = true,
            iconRes = R.drawable.ic_fav_videos,
            clickedIconRes = R.drawable.ic_fav_videos_selected,
            text = R.string.fav_videos,
            highlightedTextColor = DentelBrightBlue,
            color = DentelBrightBlue,
        )
        Spacer(modifier = Modifier.width(24.dp))
        //Fav Articles
        FavoriteButton(
            onClick = { /* TODO */ },
            clicked = true,
            iconRes = R.drawable.ic_fav_articles,
            clickedIconRes = R.drawable.ic_fav_articles_selected,
            text = R.string.fav_articles,
            highlightedTextColor = DentelLightPurple,
            color = DentelLightPurple,
        )
    }
}

@Composable
fun FavoriteButton(
    onClick: () -> Unit,
    clicked: Boolean,
    @DrawableRes iconRes: Int,
    @DrawableRes clickedIconRes: Int,
    @StringRes text: Int,
    highlightedTextColor: Color,
    color: Color,
    modifier: Modifier = Modifier
) {
    val fullTitle = stringResource(text)
    val lines = fullTitle.split("\n")

    Button(
        onClick = onClick,
        modifier = modifier
            .width(140.dp)
            .height(120.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(size = 18.dp)
            ),
        shape = RoundedCornerShape(size = 18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (clicked) color else Color(0xFFFFFFFF),
            contentColor = DentelBrightBlue
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = if (clicked) clickedIconRes else iconRes),
                contentDescription = null,
                modifier = Modifier.size(42.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))


            if (lines.isNotEmpty()) {
                Text(
                    text = lines[0],
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                        fontWeight = FontWeight(700),
                        color = if (clicked) White else highlightedTextColor,
                        textAlign = TextAlign.Center,
                    )
                )
            }

            if (lines.size > 1) {
                Text(
                    text = lines[1],
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                        fontWeight = FontWeight(700),
                        color = DentelDarkPurple,
                        textAlign = TextAlign.Center,
                    )
                )
            }
        }
    }
}

@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState
) {
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
                        .height(180.dp)
                        .background(
                            color = Color(0xFF421882),
                            shape = RoundedCornerShape(
                                bottomStart = 50.dp,
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_tooth_bg),
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
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
        ProfilePictureWithName(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            userName = uiState.userName,
            profilePictureUrl = uiState.profilePictureUrl
        )
    }
}

@Composable
fun ProfilePictureWithName(
    modifier: Modifier = Modifier,
    userName: String,
    profilePictureUrl: String?
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(160.dp),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(
                width = 4.dp,
                color = Color.White
            )
        ) {
            AsyncImage(
                model = profilePictureUrl,
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = userName,
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                fontWeight = FontWeight(500),
                color = Color(0xFF421882),
                textAlign = TextAlign.Center,
            )
        )
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