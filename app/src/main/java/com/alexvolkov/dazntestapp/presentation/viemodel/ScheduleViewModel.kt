package com.alexvolkov.dazntestapp.presentation.viemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.alexvolkov.dazntestapp.domain.FetchEventsUseCase
import com.alexvolkov.dazntestapp.domain.FetchScheduleUseCase
import com.alexvolkov.dazntestapp.presentation.data.EventItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class ScheduleViewModel(
    private val fetchScheduleUseCase: FetchScheduleUseCase
) : ViewModel() {

    val scheduleEventsFlow: Flow<PagingData<EventItem>> = flow {
        fetchScheduleUseCase.fetchSchedule()
        val pagingDataFlow = fetchScheduleUseCase.fetchSchedule()
            .map { pagingData ->
                pagingData.map { event ->
                    EventItem(
                        id = event.id,
                        title = event.title,
                        date = event.date,
                        imageUrl = event.imageUrl,
                        description = event.subtitle,
                        playable = false
                    )
                }
            }
            .cachedIn(viewModelScope)
        emitAll(pagingDataFlow)
    }.shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    init {
        println("HUI init ScheduleViewModel")
    }
}