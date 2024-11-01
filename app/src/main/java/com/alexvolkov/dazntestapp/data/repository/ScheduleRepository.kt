package com.alexvolkov.dazntestapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexvolkov.dazntestapp.data.api.RetrofitInstance
import com.alexvolkov.dazntestapp.domain.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ScheduleRepository {

    private var allEvents: List<Event> = emptyList()
    private var pager: Pager<Int, Event>? = null

    suspend fun fetchScheduleEvents() {
        allEvents = RetrofitInstance.scheduleApi.getSchedule()
        pager = Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSource(allEvents) }
        )
    }

    fun getScheduleStream(): Flow<PagingData<Event>> {
        return pager?.flow ?: flowOf()
    }
}