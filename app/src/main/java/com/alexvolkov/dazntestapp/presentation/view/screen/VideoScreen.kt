package com.alexvolkov.dazntestapp.presentation.view.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.alexvolkov.dazntestapp.presentation.viemodel.VideoPlaybackViewModel
import com.alexvolkov.dazntestapp.presentation.view.components.ShaderLoadingIndicator
import org.koin.androidx.compose.koinViewModel

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
        VideoView(viewModel.getMediaController()!!)
    }
}

@Composable
private fun VideoView(exoPlayer: MediaController) {
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

    Crossfade(isVideoReady, animationSpec = tween(1000)) {
        when (it) {
            true -> {
                Box {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(8.dp)),
                        factory = { playerView }
                    )
                }
            }

            false -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    ShaderLoadingIndicator(textColor = Color.White)
                }
            }
        }
    }
}