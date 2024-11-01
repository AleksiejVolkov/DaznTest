package com.alexvolkov.dazntestapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexvolkov.dazntestapp.data.api.ScheduleApi
import com.alexvolkov.dazntestapp.data.entity.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ScheduleRepository(
    private val apiService: ScheduleApi
) {

    private var allEvents: List<Event> = emptyList()
    private var pager: Pager<Int, Event>? = null

    suspend fun fetchScheduleEvents() {
        allEvents = apiService.getSchedule()
        pager = Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSource(allEvents.sortedBy { it.date }) }
        )
    }

    fun getScheduleStream(): Flow<PagingData<Event>> {
        return pager?.flow ?: flowOf()
    }
}