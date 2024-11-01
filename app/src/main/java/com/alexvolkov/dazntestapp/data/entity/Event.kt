package com.alexvolkov.dazntestapp.data.entity

import java.util.Date

data class Event(
    val id: String,
    val title: String,
    val subtitle: String,
    val date: Date,
    val imageUrl: String,
    val videoUrl: String
)