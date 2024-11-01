package com.alexvolkov.dazntestapp.domain

import androidx.paging.PagingData
import com.alexvolkov.dazntestapp.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class FetchScheduleUseCase(
    private val repository: ScheduleRepository
) {

    suspend fun fetchSchedule(): Flow<PagingData<Event>> {
        repository.fetchScheduleEvents()
        return repository.getScheduleStream()
    }
}