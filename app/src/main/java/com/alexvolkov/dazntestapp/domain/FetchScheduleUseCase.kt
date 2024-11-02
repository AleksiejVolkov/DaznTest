package com.alexvolkov.dazntestapp.domain

import androidx.paging.PagingData
import com.alexvolkov.dazntestapp.data.entity.Event
import com.alexvolkov.dazntestapp.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class FetchScheduleUseCase(
    private val repository: ScheduleRepository
) {

    fun fetchSchedule(): Flow<PagingData<Event>> {
        return repository.getScheduleStream()
    }
}