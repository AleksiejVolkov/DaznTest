package com.alexvolkov.dazntestapp.domain

import androidx.paging.PagingData
import com.alexvolkov.dazntestapp.data.repository.EventsRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date

class FetchEventsUseCase(
    private val repository: EventsRepository
) {

    suspend fun fetchEvents(): Flow<PagingData<Event>> {
        repository.fetchAllEvents()
        return repository.getEventsStream()
    }
}

data class Event(
    val id: String,
    val title: String,
    val subtitle: String,
    val date: Date,
    val imageUrl: String,
    val videoUrl: String
)

