package com.m7md7sn.dentel.presentation.ui.video

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.model.SuggestedTopic
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple
import com.m7md7sn.dentel.presentation.ui.home.SubtitleWithLogo
import com.m7md7sn.dentel.presentation.ui.home.SuggestedTopicsList
import com.m7md7sn.dentel.presentation.ui.section.Topic
import com.m7md7sn.dentel.presentation.ui.video.components.ArticleVideoTitle
import com.m7md7sn.dentel.presentation.ui.video.components.VideoDescriptionWithLikeAndShareButtons
import com.m7md7sn.dentel.presentation.ui.video.components.YouTubePlayerViewWrapper
import com.m7md7sn.dentel.presentation.ui.video.components.extractYoutubeVideoId

@Composable
fun VideoScreen(
    topic: Topic?,
    modifier: Modifier = Modifier,
    viewModel: VideoViewModel = hiltViewModel()
) {
    val currentLanguage = LocalConfiguration.current.locale.language
    val lifecycleOwner = LocalLifecycleOwner.current
    var playbackPosition by rememberSaveable { mutableFloatStateOf(0f) }
    val context = LocalContext.current
    val activity = context as? Activity
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val window = (LocalView.current.context as ComponentActivity).window

    // Full screen mode for better video viewing
    SideEffect {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
        }
    }

    DisposableEffect(Unit) {
        // Load video from topic when screen is first displayed
        if (topic != null) {
            viewModel.loadVideoFromTopic(topic)
        }

        onDispose {
            // Restore system bars when leaving the screen
            WindowCompat.getInsetsController(window, window.decorView).apply {
                show(WindowInsetsCompat.Type.statusBars())
                show(WindowInsetsCompat.Type.navigationBars())
            }
        }
    }

    // Observe UI state
    val uiState by viewModel.uiState.collectAsState()
    val videoId = topic?.videoUrl?.let { extractYoutubeVideoId(it) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = DentelDarkPurple
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Display title from Topic or fetched Video
            ArticleVideoTitle(title = uiState.video?.title ?: topic?.title ?: "")

            HorizontalDivider(
                Modifier.width(330.dp),
                color = Color(0xFF444B88),
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Display YouTube Player or Placeholder
            if (videoId != null) {
                YouTubePlayerViewWrapper(
                    videoId = videoId,
                    lifecycleOwner = lifecycleOwner,
                    playbackPosition = playbackPosition,
                    onPlaybackPositionChanged = { newPosition -> playbackPosition = newPosition },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .then(if (isLandscape) Modifier.fillMaxHeight() else Modifier),
                    activity = activity
                )
            } else {
                // Fallback placeholder
                Image(
                    painter = painterResource(id = R.drawable.ic_video_placeholder),
                    contentDescription = "Video Player Placeholder",
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Video description with like/share buttons
            VideoDescriptionWithLikeAndShareButtons(
                description = uiState.video?.description ?: topic?.content ?: "",
                title = uiState.video?.title ?: topic?.title ?: "",
                videoUrl = uiState.video?.videoUrl ?: topic?.videoUrl ?: ""
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Suggested topics section
            Box(modifier = Modifier.fillMaxWidth()) {
                SubtitleWithLogo(
                    titleRes = R.string.suggested_topics,
                    highlightedText = if (currentLanguage == "ar") "مواضيع" else "Topics",
                    highlightedTextColor = DentelLightPurple
                )
            }

            Spacer(Modifier.height(16.dp))

            // Suggested topics list
            SuggestedTopicsList(
                listOf(
                    SuggestedTopic(" حشوات الأسنان \nكل ما تريد معرفته عنها"),
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
