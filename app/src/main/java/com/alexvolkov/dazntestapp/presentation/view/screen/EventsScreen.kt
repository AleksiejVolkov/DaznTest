package com.alexvolkov.dazntestapp.presentation.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.alexvolkov.dazntestapp.presentation.navigation.VideoScreen
import com.alexvolkov.dazntestapp.presentation.viemodel.EventsViewModel
import com.alexvolkov.dazntestapp.presentation.view.components.ShaderLoadingIndicator
import com.alexvolkov.dazntestapp.presentation.view.components.VideoItemList
import com.alexvolkov.dazntestapp.util.CheckInternetConnection

@Composable
fun EventsScreen(
    modifier: Modifier,
    innerPaddings: PaddingValues,
    vm: EventsViewModel,
    navController: NavController
) {
    val events = vm.eventsFlow.collectAsLazyPagingItems()

    val isLoading = events.loadState.refresh is LoadState.Loading
    val isError = events.loadState.refresh is LoadState.Error
    var hasConnection by remember { mutableStateOf(true) }

    CheckInternetConnection {
        hasConnection = it
        if (hasConnection && isLoading.not() && events.itemCount == 0) {
            events.refresh()
        }
    }

    val listState = rememberSaveable(vm, saver = LazyListState.Saver) {
        LazyListState()
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.size(200.dp)) {
                ShaderLoadingIndicator()
            }
        }
    } else if (isError) {
        val error = (events.loadState.refresh as LoadState.Error).error
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Error: ${error.localizedMessage}", textAlign = TextAlign.Center)
                Button(onClick = {
                    if (hasConnection) {
                        events.retry()
                    }
                }) {
                    Text(text = "Retry")
                }
            }
        }
    } else {
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
}