package com.alexvolkov.dazntestapp.presentation.view

import android.graphics.Color
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current

    // State to track if the video is ready
    var isVideoReady by remember { mutableStateOf(false) }

    // Remember ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
        }
    }

    // Remember PlayerView instance
    val playerView = remember {
        PlayerView(context).apply {
            player = exoPlayer
            // Optionally hide default controller
            useController = true // Set to false if you don't want default controls
            // Set background color to transparent
            setShutterBackgroundColor(Color.TRANSPARENT)
        }
    }

    // Manage ExoPlayer lifecycle and listen for playback state changes
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    isVideoReady = true
                }
            }
        }

        exoPlayer.addListener(listener)
        exoPlayer.playWhenReady = true

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    if (isVideoReady) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp)),
            factory = { playerView }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp))
                .background(androidx.compose.ui.graphics.Color.Black),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}