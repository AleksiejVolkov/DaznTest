package com.alexvolkov.dazntestapp.presentation.view

import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.os.Build
import android.widget.ImageView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil3.BitmapImage
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter.State.Empty.painter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.size.Size
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
            val bitmap = (result.image as BitmapImage).bitmap
            imageBitmap = bitmap.asImageBitmap()
        }
    }

    if (imageBitmap != null) {
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
                    .blur(6.dp),
                alignment = BottomCenter,
                contentDescription = labelText, contentScale = ContentScale.FillWidth
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        color = Color.Black.copy(0.3f)
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
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}