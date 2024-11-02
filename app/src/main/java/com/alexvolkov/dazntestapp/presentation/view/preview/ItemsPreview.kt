package com.alexvolkov.dazntestapp.presentation.view.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.alexvolkov.dazntestapp.presentation.data.EventItem
import com.alexvolkov.dazntestapp.presentation.view.components.VideoItemList
import com.alexvolkov.dazntestapp.ui.theme.DaznTestAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import java.util.Date

// Mock data
val mockEventItems = listOf(
    EventItem(
        id = "1",
        title = "Event 1",
        description = "Description 1",
        date = Date(),
        imageUrl = "",
        videoUrl = ""
    ),
    EventItem(
        id = "2",
        title = "Event 2",
        description = "Description 2",
        date = Date(),
        imageUrl = "",
        videoUrl = ""
    ),
    EventItem(
        id = "3",
        title = "Event 3",
        description = "Description 3",
        date = Date(),
        imageUrl = "",
        videoUrl = ""
    )
)

@Composable
fun <T : Any> mockLazyPagingItems(items: List<T>): Flow<PagingData<T>> {
    val pagingData: Flow<PagingData<T>> = flowOf(PagingData.from(items))
    return runBlocking { pagingData }
}

@Composable
@Preview
fun ItemsPreview() {
    val mockedItems = mockLazyPagingItems(mockEventItems).collectAsLazyPagingItems()
    DaznTestAppTheme {
        VideoItemList(
            modifier = Modifier.fillMaxSize(),
            lazyListState = LazyListState(),
            topOffset = 0.dp,
            itemsData = mockedItems
        )
    }
}