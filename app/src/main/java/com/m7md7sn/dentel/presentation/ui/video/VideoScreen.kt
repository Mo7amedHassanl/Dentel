package com.m7md7sn.dentel.presentation.ui.video

import android.app.Activity
import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import androidx.compose.ui.viewinterop.AndroidView
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
import com.m7md7sn.dentel.presentation.ui.section.Topic
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

@Composable
fun VideoScreen(topic: Topic?, modifier: Modifier = Modifier) {
    val currentLanguage = LocalConfiguration.current.locale.language
    val lifecycleOwner = LocalLifecycleOwner.current
    var playbackPosition by rememberSaveable { mutableFloatStateOf(0f) }
    val context = LocalContext.current
    val activity = context as? Activity
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val window = (LocalView.current.context as ComponentActivity).window

    SideEffect {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            WindowCompat.getInsetsController(window, window.decorView).apply {
                show(WindowInsetsCompat.Type.statusBars())
                show(WindowInsetsCompat.Type.navigationBars())
            }
        }
    }

    val videoId = topic?.videoUrl?.let { extractYoutubeVideoId(it) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = DentelDarkPurple
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ArticleVideoTitle(title = topic?.title ?: "")
            HorizontalDivider(
                Modifier
                    .width(330.dp),
                color = Color(0xFF444B88),
            )
            Spacer(modifier = Modifier.height(28.dp))

            if (videoId != null) {
                YouTubePlayerViewWrapper(
                    videoId = videoId,
                    lifecycleOwner = lifecycleOwner,
                    playbackPosition = playbackPosition,
                    onPlaybackPositionChanged = { newPosition -> playbackPosition = newPosition },
                    modifier = Modifier.fillMaxWidth().height(250.dp).then(if (isLandscape) Modifier.fillMaxHeight() else Modifier),
                    activity = activity
                )
            } else {
                // Fallback to placeholder if videoId is null or invalid
                Image(
                    painter = painterResource(id = R.drawable.ic_video_placeholder),
                    contentDescription = "Video Player Placeholder",
                    modifier = Modifier
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
            VideoDescriptionWithLikeAndShareButtons(description = topic?.content ?: "")
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
fun YouTubePlayerViewWrapper(
    videoId: String,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    playbackPosition: Float,
    onPlaybackPositionChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    activity: Activity?
) {
    AndroidView(
        factory = { context ->
            YouTubePlayerView(context).apply {
                lifecycleOwner.lifecycle.addObserver(this)
                // We set enableAutomaticInitialization to false, so we must initialize manually
                enableAutomaticInitialization = false
                // Ensure default player UI is used
                // Removed: controller.showYouTubePlayerControls(true)
                // Removed: fitsSystemWindows = true

                this.initialize(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(videoId, playbackPosition)
                        youTubePlayer.play()
                        // Explicitly show fullscreen button after player is ready
                        // Removed: youTubePlayer.getYouTubePlayerUiController().showFullscreenButton(true)
                        // Removed: youTubePlayer.getYouTubePlayerUiController().showYouTubePlayerControls(true)
                    }

                    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                        onPlaybackPositionChanged(second)
                    }

                    override fun onStateChange(youTubePlayer: YouTubePlayer, state: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState) {
                        // Handle player state changes, e.g., error handling or buffering
                    }
                })
            }
        },
        modifier = modifier
    )
}

private fun extractYoutubeVideoId(youtubeUrl: String): String? {
    val regex = ".*v=([a-zA-Z0-9_-]+).*".toRegex()
    val matchResult = regex.find(youtubeUrl)
    return matchResult?.groups?.get(1)?.value
}

@Composable
fun VideoDescriptionWithLikeAndShareButtons(description: String, modifier: Modifier = Modifier) {
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
                text = description,
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
            fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
            color = Color.White
        ),
        modifier = modifier.padding(16.dp)
    )
}

@Preview
@Composable
private fun VideoScreenPreviewEn() {
    DentelTheme {
        VideoScreen(null)
    }
}

@Preview(locale = "ar")
@Composable
private fun VideoScreenPreviewAr() {
    DentelTheme {
        VideoScreen(null)
    }
}