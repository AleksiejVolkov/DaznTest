package com.alexvolkov.dazntestapp.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.alexvolkov.dazntestapp.presentation.data.EventItem
import com.alexvolkov.dazntestapp.presentation.viemodel.EventsViewModel
import com.alexvolkov.dazntestapp.util.Utils.formatDate

@Composable
fun EventsList(
    modifier: Modifier = Modifier,
    innerPaddings: PaddingValues,
    eventsViewModel: EventsViewModel,
    onOpenVideo: (String) -> Unit
) {
    val events = eventsViewModel.eventsFlow.collectAsLazyPagingItems()

    val listState = rememberSaveable(eventsViewModel, saver = LazyListState.Saver) {
        LazyListState()
    }

    Box {
        LazyColumn(
            modifier = modifier.padding(horizontal = 8.dp), state = listState
        ) {
            item {
                Spacer(modifier = Modifier.height(innerPaddings.calculateTopPadding()))
            }
            items(count = events.itemCount,
                key = events.itemKey { it.id },
                contentType = events.itemContentType { "contentType" }) { index ->
                events[index]?.let {
                    EventCard(it) { videoUrl ->
                        onOpenVideo(videoUrl)
                    }
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

@Composable
fun EventCard(
    event: EventItem, onOpenVideo: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.large
    ) {
        Column {
            Box {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(MaterialTheme.shapes.small),
                    model = event.imageUrl,
                    contentScale = androidx.compose.ui.layout.ContentScale.FillWidth,
                    contentDescription = null,
                )
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(bottomEnd = 5.dp)
                        )
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = androidx.compose.ui.Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatDate(event.date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                if (event.imageUrl.isNotEmpty()) {
                    OutlinedButton(
                        onClick = { onOpenVideo(event.videoUrl) },
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(text = "Watch")
                    }
                }
            }
        }
    }
}