package com.alexvolkov.dazntestapp.presentation.viemodel

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.alexvolkov.dazntestapp.service.MediaPlayerService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VideoPlaybackViewModel : ViewModel() {
    private var mediaController: MediaController? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null

    val _state: MutableStateFlow<VideoPlaybackState> = MutableStateFlow(VideoPlaybackState())
    var viewState: StateFlow<VideoPlaybackState> = _state

    init {
        println("init VideoPlaybackViewModel ${hashCode()}")
    }

    fun connectToService(applicationContext: Context, videoUrl: String) {
        val token = SessionToken(
            applicationContext,
            ComponentName(applicationContext, MediaPlayerService::class.java)
        )

        controllerFuture = MediaController.Builder(applicationContext, token).buildAsync()
        controllerFuture?.addListener(
            {
                if (mediaController != null) return@addListener

                mediaController = controllerFuture?.get()
                loadMedia(videoUrl)
                _state.apply { value = VideoPlaybackState(ready = true) }
            },
            MoreExecutors.directExecutor()
        )
    }

    private fun loadMedia(videoUrl: String) {
        val mediaItem = androidx.media3.common.MediaItem.Builder()
            .setUri(videoUrl)
            .build()
        mediaController?.setMediaItem(mediaItem)
        mediaController?.prepare()
        mediaController?.play()
    }

    fun getMediaController(): MediaController? {
        return mediaController
    }

    fun release() {
        println("called release in VideoPlaybackViewModel $mediaController, $controllerFuture")
        controllerFuture?.cancel(true)
        mediaController?.stop()
        mediaController?.release()
        mediaController = null
    }

    override fun onCleared() {
        super.onCleared()
        release()
    }
}

data class VideoPlaybackState(
    val ready: Boolean = false
)