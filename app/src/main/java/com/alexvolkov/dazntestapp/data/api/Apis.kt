package com.alexvolkov.dazntestapp.data.api

import com.alexvolkov.dazntestapp.domain.Event
import retrofit2.http.GET

interface EventsApi {
    @GET("getEvents")
    suspend fun getAllEvents(): List<Event>
}

interface ScheduleApi {
    @GET("getSchedule")
    suspend fun getSchedule(): List<Event>
}
