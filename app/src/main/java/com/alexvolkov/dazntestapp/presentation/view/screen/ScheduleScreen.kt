package com.alexvolkov.dazntestapp.presentation.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.paging.compose.collectAsLazyPagingItems
import com.alexvolkov.dazntestapp.presentation.viemodel.ScheduleViewModel
import com.alexvolkov.dazntestapp.presentation.view.components.VideoItemList
import kotlinx.coroutines.delay

@Composable
fun ScheduleScreen(
    modifier: Modifier,
    innerPaddings: PaddingValues,
    vm: ScheduleViewModel
) {
    val events = vm.scheduleEventsFlow.collectAsLazyPagingItems()

    val listState = rememberSaveable(vm, saver = LazyListState.Saver) {
        LazyListState()
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(30_000)
            events.refresh()
        }
    }

    Box {
        VideoItemList(
            modifier = modifier,
            lazyListState = listState,
            topOffset = innerPaddings.calculateTopPadding(),
            itemsData = events
        )
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