package com.alexvolkov.dazntestapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexvolkov.dazntestapp.data.api.RetrofitInstance
import com.alexvolkov.dazntestapp.domain.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EventsRepository {

    private var allEvents: List<Event> = emptyList()
    private var pager: Pager<Int, Event>? = null

    suspend fun fetchAllEvents() {
        allEvents = RetrofitInstance.eventsApi.getAllEvents()
        pager = Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSource(allEvents.sortedBy { it.date }) }
        )
    }

    fun getEventsStream(): Flow<PagingData<Event>> {
        return pager?.flow ?: flowOf()
    }
}