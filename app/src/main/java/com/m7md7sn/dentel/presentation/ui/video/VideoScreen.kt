package com.m7md7sn.dentel.presentation.ui.video

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.model.SuggestedTopic
import com.m7md7sn.dentel.data.model.Video
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.home.SubtitleWithLogo
import com.m7md7sn.dentel.presentation.ui.home.SuggestedTopicsList
import androidx.lifecycle.ViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@Composable
fun VideoScreen(title: String, subtitle: String, modifier: Modifier = Modifier, viewModel: VideoViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    // Set video on first composition
    LaunchedEffect(title, subtitle) {
        viewModel.setVideo(Video(id = "", title = title, subtitle = subtitle, videoUrl = ""))
    }

    val video = uiState.video
    val currentLanguage = LocalConfiguration.current.locale.language

    Surface(
        modifier = modifier.fillMaxSize(),
        color = DentelDarkPurple
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ArticleVideoTitle(title = video?.title ?: "")
            HorizontalDivider(
                Modifier
                    .width(330.dp),
                color = Color(0xFF444B88),
            )
            Spacer(modifier = Modifier.height(28.dp))
            // Placeholder for video player
            Image(
                painter = painterResource(id = R.drawable.ic_video_placeholder),
                contentDescription = "Video Player Placeholder",
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(28.dp))
            VideoDescriptionWithLikeAndShareButtons(subtitle = video?.subtitle ?: "")
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                SubtitleWithLogo(
                    titleRes = R.string.suggested_topics,
                    highlightedText = if (currentLanguage == "ar") "مواضيع" else "Topics",
                    highlightedTextColor = DentelLightPurple
                )
            }
            Spacer(Modifier.height(16.dp))
            SuggestedTopicsList(
                listOf(
                    SuggestedTopic(
                        " حشوات الأسنان \n" +
                                "كل ما تريد معرفته عنها"
                    ),
                    SuggestedTopic("Root Canal Treatment"),
                    SuggestedTopic("Teeth Whitening"),
                    SuggestedTopic("Orthodontics"),
                    SuggestedTopic("Cosmetic Dentistry"),
                    SuggestedTopic("Oral Hygiene Tips")
                )
            )
        }
    }
}

@Composable
fun VideoDescriptionWithLikeAndShareButtons(subtitle: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                ) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        color = DentelDarkPurple,
                        shape = RoundedCornerShape(bottomEnd = 30.dp)
                    ) {}
                    Surface(
                        modifier = Modifier.size(30.dp),
                        color = DentelDarkPurple,
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 30.dp)
                        ) {}
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(
                        modifier = Modifier.size(30.dp),
                        color = DentelDarkPurple,
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.White,
                            shape = RoundedCornerShape(topEnd = 30.dp)
                        ) {}
                    }
                    Surface(
                        modifier = Modifier.size(60.dp),
                        color = DentelDarkPurple,
                        shape = RoundedCornerShape(bottomStart = 30.dp)
                    ) {}
                }
            }
            LikeAndShareButtons(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(bottomEnd = 30.dp, bottomStart = 30.dp))
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(
                text = subtitle,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 25.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Start,
                    letterSpacing = 0.38.sp,
                )
            )
        }
    }
}

@Composable
fun LikeAndShareButtons(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VideoButton(
            text = R.string.like,
            onClick = { /* Handle like action */ },
            icon = Icons.Outlined.Favorite,
            tint = Color(0xFFE63E3E),
        )
        Spacer(modifier = Modifier.width(16.dp))
        VideoButton(
            text = R.string.share,
            onClick = { /* Handle share action */ },
            icon = Icons.Outlined.Share, // Replace with a share icon
            tint = Color.Black,
        )
    }
}

@Composable
private fun VideoButton(
    @StringRes text: Int,
    onClick: () -> Unit,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .height(40.dp)
            .width(94.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = DentelDarkPurple,
        ),
        border = BorderStroke(
            width = 0.5.dp,
            color = Color(0xFFAE9FC6)
        ),
        contentPadding = PaddingValues(8.dp)
    ) {
        Text(
            text = stringResource(text),
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                fontWeight = FontWeight(500),
                color = Color(0xFF707070),
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ArticleVideoTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        ),
        modifier = modifier.padding(16.dp)
    )
}

@Preview
@Composable
private fun VideoScreenPreviewEn() {
    DentelTheme {
        VideoScreen("Video Title", "Video Subtitle")
    }
}

@Preview(locale = "ar")
@Composable
private fun VideoScreenPreviewAr() {
    DentelTheme {
        VideoScreen("Video Title", "Video Subtitle")
    }
}