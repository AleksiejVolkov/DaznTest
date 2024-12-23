package com.alexvolkov.dazntestapp.presentation.viemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.alexvolkov.dazntestapp.domain.FetchEventsUseCase
import com.alexvolkov.dazntestapp.presentation.data.EventItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class EventsViewModel(
    private val fetchEventsUseCase: FetchEventsUseCase
) : ViewModel() {

    val eventsFlow: Flow<PagingData<EventItem>> = flow {
        val pagingDataFlow = fetchEventsUseCase.fetchEvents()
            .map { pagingData ->
                pagingData.map { event ->
                    EventItem(
                        id = event.id,
                        title = event.title,
                        date = event.date,
                        imageUrl = event.imageUrl,
                        videoUrl = event.videoUrl,
                        description = event.subtitle
                    )
                }
            }
            .cachedIn(viewModelScope)
        emitAll(pagingDataFlow)
    }.shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

}
