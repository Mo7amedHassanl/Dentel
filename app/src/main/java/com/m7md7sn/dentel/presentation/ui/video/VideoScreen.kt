package com.m7md7sn.dentel.presentation.ui.video

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.model.SuggestedTopic
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple
import com.m7md7sn.dentel.presentation.ui.home.components.SubtitleWithLogo
import com.m7md7sn.dentel.presentation.ui.home.components.SuggestedTopicsList

import com.m7md7sn.dentel.presentation.ui.section.Topic
import com.m7md7sn.dentel.presentation.ui.section.TopicType
import com.m7md7sn.dentel.presentation.ui.video.components.ArticleVideoTitle
import com.m7md7sn.dentel.presentation.ui.video.components.VideoDescriptionWithLikeAndShareButtons
import com.m7md7sn.dentel.presentation.ui.video.components.YouTubePlayerViewWrapper
import com.m7md7sn.dentel.presentation.ui.video.components.extractYoutubeVideoId

@Composable
fun VideoScreen(
    topic: Topic?,
    modifier: Modifier = Modifier,
    viewModel: VideoViewModel = hiltViewModel(),
    onNavigateToVideo: (Topic) -> Unit = {},
    onNavigateToArticle: (Topic) -> Unit = {}
) {
    val currentLanguage = LocalConfiguration.current.locale.language
    val lifecycleOwner = LocalLifecycleOwner.current
    var playbackPosition by rememberSaveable { mutableFloatStateOf(0f) }
    val context = LocalContext.current
    val activity = context as? Activity
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val window = (LocalView.current.context as ComponentActivity).window

    // Observe UI state and navigation state
    val uiState by viewModel.uiState.collectAsState()
    val navigationState by viewModel.navigationState.collectAsState()
    val videoId = topic?.videoUrl?.let { extractYoutubeVideoId(it) }

    // Handle navigation based on navigation state
    LaunchedEffect(navigationState) {
        when (val state = navigationState) {
            is NavigationState.NavigateToContent -> {
                when (state.topic.type) {
                    TopicType.Video -> onNavigateToVideo(state.topic)
                    TopicType.Article -> onNavigateToArticle(state.topic)
                }
                viewModel.resetNavigationState()
            }
            is NavigationState.Error -> {
                // Could show a snackbar or other error indicator here
            }
            else -> { /* No action needed */ }
        }
    }

    // Load video from topic when screen is first displayed
    DisposableEffect(Unit) {
        if (topic != null) {
            viewModel.loadVideoFromTopic(topic)
        }
        onDispose { }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = DentelDarkPurple
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                    videoUrl = uiState.video?.videoUrl ?: topic?.videoUrl ?: "",
                    isFavorite = uiState.isFavorite,
                    onLikeClick = { viewModel.toggleFavorite() }
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
                if (uiState.suggestedTopics.isNotEmpty()) {
                    SuggestedTopicsList(
                        topics = uiState.suggestedTopics,
                        onTopicClick = { suggestedTopic ->
                            viewModel.onSuggestedTopicClicked(suggestedTopic)
                        }
                    )
                } else if (uiState.suggestedTopicsError != null) {
                    Text(
                        text = uiState.suggestedTopicsError ?: "Failed to load suggested topics",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Show loading overlay when fetching topic
            if (navigationState is NavigationState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x80000000)), // Semi-transparent background
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = DentelDarkPurple,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}
