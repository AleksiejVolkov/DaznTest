package com.alexvolkov.dazntestapp.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FetchEventsUseCase {

    fun fetchEvents(): Flow<List<Event>> = flowOf(
        listOf(
            Event(
                id = "1",
                title = "Liverpool v Porto",
                subtitle = "UEFA Champions League",
                date = "2024-10-31T01:22:13.828Z",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310176837169_image-header_pDach_1554579780000.jpeg?alt=media&token=1777d26b-d051-4b5f-87a8-7633d3d6dd20",
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
            ),
            Event(
                id = "2",
                title = "Nîmes v Rennes",
                subtitle = "Ligue 1",
                date = "2024-10-31T02:22:13.828Z",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310381637057_image-header_pDach_1554664873000.jpeg?alt=media&token=53616931-55a8-476e-b1b7-d18fc22a2bf0",
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
            ),
            Event(
                id = "3",
                title = "Tottenham v Man City",
                subtitle = "UEFA Champions League",
                date = "2024-10-31T03:22:13.828Z",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310511685198_image-header_pDach_1554872450000.jpeg?alt=media&token=5524d719-261e-49e6-abf3-a74c30df3e27",
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
            ),
            Event(
                id = "4",
                title = "Suns @ Mavericks",
                subtitle = "NBA",
                date = "2024-10-31T04:22:13.828Z",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310572613233_image-header_pDach_1554812508000.jpeg?alt=media&token=4ee99b47-dcae-4016-b213-54e14a0d40d8",
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
            ),
            Event(
                id = "5",
                title = "Chelsea v West Ham",
                subtitle = "Premier League",
                date = "2024-10-31T05:22:13.828Z",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310632005268_image-header_pDach_1554838415000.jpeg?alt=media&token=5a512da9-d268-432c-9da2-088c8e6737e1",
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
            )
        )
    )
}

data class Event(
    val id: String,
    val title: String,
    val subtitle: String,
    val date: String,
    val imageUrl: String,
    val videoUrl: String
)