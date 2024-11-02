package com.alexvolkov.dazntestapp.presentation.view.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil3.BitmapImage
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import com.alexvolkov.dazntestapp.presentation.data.EventItem
import com.alexvolkov.dazntestapp.util.Utils.formatDate

@Composable
fun VideoItemList(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    topOffset: Dp,
    itemsData: LazyPagingItems<EventItem>,
    onOpenVideo: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp), state = lazyListState
    ) {
        item {
            Spacer(modifier = Modifier.height(topOffset))
        }
        items(count = itemsData.itemCount,
            key = itemsData.itemKey { it.id },
            contentType = itemsData.itemContentType { "contentType" }) { index ->
            itemsData[index]?.let {
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
}

@Composable
fun EventCard(
    event: EventItem, onOpenVideo: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        backgroundColor = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.large
    ) {
        Column {
            Box {
                ImageWithBlurredLabel(
                    imageUrl = event.imageUrl,
                    labelText = event.title
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.Bottom
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
                if (event.videoUrl.isNotEmpty()) {
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

@Composable
fun ImageWithBlurredLabel(imageUrl: String, labelText: String) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current

    LaunchedEffect(imageUrl) {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()

        val result = loader.execute(request)
        if (result is SuccessResult) {
            imageBitmap = (result.image as BitmapImage).bitmap.asImageBitmap()
        }
    }

    Crossfade(imageBitmap, animationSpec = tween(1000)) { bitmap ->
        when (bitmap) {
            null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(color = MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    DaznLogoLoadingIndicator(modifier = Modifier.size(120.dp))
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentAlignment = BottomCenter
                ) {
                    Image(
                        bitmap = imageBitmap!!,
                        modifier = Modifier.fillMaxSize(),
                        alignment = BottomCenter,
                        contentDescription = labelText, contentScale = ContentScale.FillWidth
                    )
                    Image(
                        bitmap = imageBitmap!!,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .blur(9.dp),
                        alignment = BottomCenter,
                        contentDescription = labelText, contentScale = ContentScale.FillWidth
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(
                                color = Color.Black.copy(0.4f)
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = labelText,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}