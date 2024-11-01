package com.alexvolkov.dazntestapp.presentation.data

import java.util.Date

data class EventItem(
    val id: String,
    val title: String,
    val description: String,
    val date: Date,
    val imageUrl: String,
    val videoUrl: String,
    val playable: Boolean = false
)