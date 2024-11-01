package com.alexvolkov.dazntestapp.presentation.viemodel

import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer

class VideoPlaybackViewModel : ViewModel() {
    var exoPlayer: ExoPlayer? = null
    var playbackPosition: Long = 0L
    var playWhenReady: Boolean = true

    override fun onCleared() {
        super.onCleared()
        // Release the ExoPlayer when the ViewModel is cleared
        exoPlayer?.release()
        exoPlayer = null
    }
}