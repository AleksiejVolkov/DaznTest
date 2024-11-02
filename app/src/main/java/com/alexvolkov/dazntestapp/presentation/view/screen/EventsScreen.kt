package com.alexvolkov.dazntestapp.presentation.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.alexvolkov.dazntestapp.presentation.navigation.VideoScreen
import com.alexvolkov.dazntestapp.presentation.viemodel.EventsViewModel
import com.alexvolkov.dazntestapp.presentation.view.components.VideoItemList

@Composable
fun EventsScreen(
    modifier: Modifier,
    innerPaddings: PaddingValues,
    vm: EventsViewModel,
    navController: NavController
) {
    val events = vm.eventsFlow.collectAsLazyPagingItems()

    val listState = rememberSaveable(vm, saver = LazyListState.Saver) {
        LazyListState()
    }

    Box {
        VideoItemList(
            modifier = modifier,
            lazyListState = listState,
            topOffset = innerPaddings.calculateTopPadding(),
            itemsData = events
        ) {
            navController.navigate(VideoScreen(it))
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