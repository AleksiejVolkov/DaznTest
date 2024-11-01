package com.alexvolkov.dazntestapp.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.alexvolkov.dazntestapp.presentation.viemodel.ScheduleViewModel
import kotlinx.coroutines.delay

@Composable
fun ScheduleList(
    modifier: Modifier = Modifier,
    innerPaddings: PaddingValues,
    scheduleViewModel: ScheduleViewModel
) {
    val events = scheduleViewModel.scheduleEventsFlow.collectAsLazyPagingItems()

    val listState = rememberSaveable(scheduleViewModel, saver = LazyListState.Saver) {
        LazyListState()
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(30_000)
            events.refresh()
        }
    }

    Box {
        LazyColumn(
            modifier = modifier.padding(horizontal = 8.dp),
            state = listState
        ) {
            item {
                Spacer(modifier = Modifier.height(innerPaddings.calculateTopPadding()))
            }
            items(
                count = events.itemCount,
                key = events.itemKey { it.id },
                contentType = events.itemContentType { "contentType" }
            ) { index ->
                events[index]?.let {
                    EventCard(it)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(innerPaddings.calculateTopPadding())
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0f)
                        ),
                    )
                )
        )
    }
}