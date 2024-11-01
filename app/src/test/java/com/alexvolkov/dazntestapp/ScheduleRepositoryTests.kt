package com.alexvolkov.dazntestapp

import androidx.paging.testing.asSnapshot
import com.alexvolkov.dazntestapp.data.api.EventsApi
import com.alexvolkov.dazntestapp.data.api.ScheduleApi
import com.alexvolkov.dazntestapp.data.entity.Event
import com.alexvolkov.dazntestapp.data.repository.EventsRepository
import com.alexvolkov.dazntestapp.data.repository.ScheduleRepository
import com.alexvolkov.dazntestapp.domain.FetchEventsUseCase
import com.alexvolkov.dazntestapp.domain.FetchScheduleUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class ScheduleRepositoryTests {

    private val apiService = mockk<ScheduleApi>()
    private val repository = ScheduleRepository(apiService)
    private val fetchScheduleUseCase = FetchScheduleUseCase(repository)

    @Test
    fun `fetchSchedule should fetch events from API and update allEvents correctly`() = runTest {
        // Arrange
        val initialEvents = listOf(
            Event(id = "1", title = "Event 1", subtitle = "Subtitle 1", date = Date(), imageUrl = "", videoUrl = ""),
            Event(id = "2", title = "Event 2", subtitle = "Subtitle 2", date = Date(), imageUrl = "", videoUrl = "")
        )
        val updatedEvents = listOf(
            Event(id = "3", title = "Event 3", subtitle = "Subtitle 3", date = Date(), imageUrl = "", videoUrl = ""),
            Event(id = "4", title = "Event 4", subtitle = "Subtitle 4", date = Date(), imageUrl = "", videoUrl = "")
        )

        coEvery { apiService.getSchedule() } returns initialEvents andThen updatedEvents

        // Act
        val initialEventsStream = fetchScheduleUseCase.fetchSchedule()
        val initialCollectedEvents = initialEventsStream.asSnapshot { }

        val updatedEventsStream = fetchScheduleUseCase.fetchSchedule()
        val updatedCollectedEvents = updatedEventsStream.asSnapshot { }

        // Assert
        assertEquals(initialEvents, initialCollectedEvents)
        assertEquals(updatedEvents, updatedCollectedEvents)
    }

}
