package com.alexvolkov.dazntestapp.service

import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class MediaPlayerService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSession.Builder(this, ExoPlayer.Builder(this).build()).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}