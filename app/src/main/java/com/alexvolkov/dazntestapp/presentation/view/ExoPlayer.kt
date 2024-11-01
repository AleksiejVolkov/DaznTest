package com.alexvolkov.dazntestapp.presentation.view

import android.graphics.Color
import androidx.activity.compose.BackHandler
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
import androidx.navigation.NavController
import com.alexvolkov.dazntestapp.presentation.viemodel.VideoPlaybackViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun VideoPlayer(
    viewModel: VideoPlaybackViewModel = koinViewModel(),
    videoUrl: String,
    navController: NavController
) {
    val context = LocalContext.current

    var isVideoReady by rememberSaveable { mutableStateOf(false) }
    var isFinishing by remember { mutableStateOf(false) }

    val exoPlayer = remember {
        viewModel.exoPlayer ?: ExoPlayer.Builder(context).build().apply {
            viewModel.exoPlayer = this
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            seekTo(viewModel.playbackPosition)
            playWhenReady = viewModel.playWhenReady
        }
    }

    // Remember PlayerView instance
    val playerView = remember(exoPlayer) {
        PlayerView(context).apply {
            player = exoPlayer
            useController = true // Set to false if you don't want default controls
        }
    }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    isVideoReady = true
                }
            }
        }

        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            // Save playback position and state
            viewModel.playbackPosition = exoPlayer.currentPosition
            viewModel.playWhenReady = exoPlayer.playWhenReady
            // Do not release the ExoPlayer here; it is managed by the ViewModel
        }
    }

    BackHandler {
        navController.popBackStack()
        isFinishing = true
    }

    if (isVideoReady && isFinishing.not()) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp)),
            factory = { playerView }
        )
    } else if (isFinishing.not()) {
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