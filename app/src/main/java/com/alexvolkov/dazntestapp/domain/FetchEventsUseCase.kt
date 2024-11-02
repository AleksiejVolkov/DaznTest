package com.alexvolkov.dazntestapp.domain

import androidx.paging.PagingData
import com.alexvolkov.dazntestapp.data.entity.Event
import com.alexvolkov.dazntestapp.data.repository.EventsRepository
import kotlinx.coroutines.flow.Flow

class FetchEventsUseCase(
    private val repository: EventsRepository
) {
    fun fetchEvents(): Flow<PagingData<Event>> {
        return repository.getEventsStream()
    }
}

