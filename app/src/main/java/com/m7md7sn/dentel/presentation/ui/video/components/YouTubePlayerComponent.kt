package com.m7md7sn.dentel.presentation.ui.video.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YouTubePlayerViewWrapper(
    videoId: String,
    lifecycleOwner: LifecycleOwner,
    playbackPosition: Float,
    onPlaybackPositionChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    activity: Activity? = null
) {
    AndroidView(
        factory = { context ->
            YouTubePlayerView(context).apply {
                lifecycleOwner.lifecycle.addObserver(this)
                enableAutomaticInitialization = false

                initialize(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(videoId, playbackPosition)
                        youTubePlayer.play()
                    }

                    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                        onPlaybackPositionChanged(second)
                    }

                    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                        // Handle player state changes if needed
                    }
                })
            }
        },
        modifier = modifier
    )
}

/**
 * Extracts the YouTube video ID from a YouTube URL.
 * Example: https://www.youtube.com/watch?v=dQw4w9WgXcQ -> dQw4w9WgXcQ
 */
fun extractYoutubeVideoId(youtubeUrl: String): String? {
    val regex = ".*v=([a-zA-Z0-9_-]+).*".toRegex()
    val matchResult = regex.find(youtubeUrl)
    return matchResult?.groups?.get(1)?.value
}
