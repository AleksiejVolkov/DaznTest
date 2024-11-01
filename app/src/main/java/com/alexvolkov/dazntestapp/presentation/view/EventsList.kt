package com.alexvolkov.dazntestapp.presentation.view

import android.text.format.DateFormat
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.alexvolkov.dazntestapp.presentation.data.EventItem
import com.alexvolkov.dazntestapp.presentation.viemodel.EventsViewModel
import com.alexvolkov.dazntestapp.util.Utils.formatDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun EventsList(
    modifier: Modifier = Modifier,
    innerPaddings: PaddingValues,
    eventsViewModel: EventsViewModel = koinViewModel()
) {
    val state = eventsViewModel.state.collectAsState()

    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        item {
            Spacer(modifier = Modifier.height(innerPaddings.calculateTopPadding()))
        }
        items(state.value.events) { event ->
            EventCard(event)
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun EventCard(event: EventItem) {
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
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
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
                if(event.playable) {
                    OutlinedButton(onClick = { /*TODO*/ },
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(text = "Watch")
                    }
                }
            }
        }
    }
}