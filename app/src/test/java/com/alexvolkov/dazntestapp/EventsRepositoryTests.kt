package com.alexvolkov.dazntestapp

import androidx.paging.testing.asSnapshot
import com.alexvolkov.dazntestapp.data.api.EventsApi
import com.alexvolkov.dazntestapp.data.entity.Event
import com.alexvolkov.dazntestapp.data.repository.EventsRepository
import com.alexvolkov.dazntestapp.domain.FetchEventsUseCase
import com.alexvolkov.dazntestapp.domain.FetchScheduleUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class EventsRepositoryTests {

    private val apiService = mockk<EventsApi>()
    private val repository = EventsRepository(apiService)

    @Test
    fun `fetchAllEvents should fetch events from API and update allEvents`() = runTest {
        // Arrange
        val mockEvents = listOf(
            Event(
                id = "1",
                title = "Event 1",
                subtitle = "Subtitle 1",
                date = Date(),
                imageUrl = "",
                videoUrl = ""
            ),
            Event(
                id = "2",
                title = "Event 2",
                subtitle = "Subtitle 2",
                date = Date(),
                imageUrl = "",
                videoUrl = ""
            )
        )

        coEvery { apiService.getAllEvents() } returns mockEvents

        // Act
        val fetchEventsUseCase = FetchEventsUseCase(repository)
        val eventsStream = fetchEventsUseCase.fetchEvents()

        val collectedEvents = eventsStream.asSnapshot {}

        assertEquals(mockEvents, collectedEvents)
    }


}
