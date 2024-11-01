package com.alexvolkov.dazntestapp.presentation.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.alexvolkov.dazntestapp.presentation.viemodel.VideoPlaybackViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.GlobalContext

@Composable
fun VideoPlayer(
    viewModel: VideoPlaybackViewModel = koinViewModel(),
    videoUrl: String,
    navController: NavController,
    onVmInited: () -> Unit
) {
    val context = LocalContext.current.applicationContext
    val viewState = viewModel.viewState.collectAsState()
    var isFinishing by remember { mutableStateOf(false) }

    val isInitialized = rememberSaveable { mutableStateOf(false) }

    if (!isInitialized.value) {
        LaunchedEffect(Unit) {
            onVmInited()
            viewModel.connectToService(context, videoUrl)
            isInitialized.value = true
        }
    }

    BackHandler {
        isFinishing = true
        viewModel.release()
        navController.popBackStack()
    }

    if (viewState.value.ready && !isFinishing) {
        VideoView(viewModel.getMediaController()!!, viewModel)
    }
}

@Composable
private fun VideoView(exoPlayer: MediaController, viewModel: VideoPlaybackViewModel) {
    val context = LocalContext.current
    var isVideoReady by rememberSaveable { mutableStateOf(false) }

    val playerView = remember(exoPlayer) {
        PlayerView(context).apply {
            player = exoPlayer
            useController = true
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