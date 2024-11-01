package com.alexvolkov.dazntestapp.presentation.viemodel

import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer

class VideoPlaybackViewModel : ViewModel() {
    var exoPlayer: ExoPlayer? = null
    var playbackPosition: Long = 0L
    var playWhenReady: Boolean = true

    override fun onCleared() {
        super.onCleared()
        exoPlayer?.release()
        exoPlayer = null
    }
}