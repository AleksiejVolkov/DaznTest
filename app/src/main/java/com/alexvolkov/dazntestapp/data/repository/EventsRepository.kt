package com.alexvolkov.dazntestapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexvolkov.dazntestapp.data.api.EventsApi
import com.alexvolkov.dazntestapp.data.entity.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EventsRepository(
    private val apiService: EventsApi
) {
    private var allEvents: List<Event> = emptyList()
    private var pager: Pager<Int, Event>? = null

    suspend fun fetchAllEvents() {
        allEvents = apiService.getAllEvents()
        if (pager == null) {
            pager = Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { PagingSource(allEvents.sortedBy { it.date }) }
            )
        }
    }

    fun getEventsStream(): Flow<PagingData<Event>> {
        return pager?.flow ?: flowOf()
    }
}