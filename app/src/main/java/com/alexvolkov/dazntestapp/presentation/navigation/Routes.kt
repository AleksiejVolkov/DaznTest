package com.alexvolkov.dazntestapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
open class Route

@Serializable
data object EventsScreen : Route()

@Serializable
data object ScheduleScreen : Route()

@Serializable
data object VideoScreen : Route()
