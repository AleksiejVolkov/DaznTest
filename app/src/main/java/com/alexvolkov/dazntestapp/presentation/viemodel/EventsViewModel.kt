package com.alexvolkov.dazntestapp.presentation.viemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexvolkov.dazntestapp.domain.FetchEventsUseCase
import com.alexvolkov.dazntestapp.presentation.data.EventItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EventsViewModel(
    fetchEventsUseCase: FetchEventsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EventsViewState())
    val state: StateFlow<EventsViewState> = _state

    init {
        viewModelScope.launch {
            fetchEventsUseCase.fetchEvents().collectLatest {
                _state.apply {
                    value = value.copy(events = it.sortedBy { it.date }.map {
                        EventItem(
                            id = it.id,
                            title = it.title,
                            date = it.date,
                            imageUrl = it.imageUrl,
                            videoUrl = it.videoUrl,
                            description = it.subtitle,
                            playable = true
                        )
                    })
                }
            }
        }
    }
}

data class EventsViewState(
    val isLoading: Boolean = false,
    val events: List<EventItem> = emptyList()
)