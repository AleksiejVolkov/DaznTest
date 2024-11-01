package com.alexvolkov.dazntestapp

// In your test class
import androidx.paging.testing.asSnapshot
import com.alexvolkov.dazntestapp.data.api.EventsApi
import com.alexvolkov.dazntestapp.data.repository.EventsRepository
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(RobolectricTestRunner::class)
class NetworkTest {

    private val mockWebServer = MockWebServer()
    private lateinit var apiService: EventsApi
    private lateinit var repository: EventsRepository

    @Before
    fun setup() {
        mockWebServer.start()
        val baseUrl = mockWebServer.url("/")

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(EventsApi::class.java)
        repository = EventsRepository(apiService)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `fetchAllEvents should fetch events from API and update allEvents`() = runTest {
        // Arrange
        val mockEventsJson = """
            [
                {"id":"1","title":"Event 1","subtitle":"Subtitle 1","date":"2023-01-01T00:00:00Z","imageUrl":"","videoUrl":""},
                {"id":"2","title":"Event 2","subtitle":"Subtitle 2","date":"2023-01-02T00:00:00Z","imageUrl":"","videoUrl":""}
            ]
        """
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(mockEventsJson)

        mockWebServer.enqueue(mockResponse)

        // Act
        repository.fetchAllEvents()

        // Assert
        val pagingData = repository.getEventsStream()
        val items = pagingData.asSnapshot {}

        assertEquals(2, items.size)
        assertEquals("Event 1", items[0].title)
    }
}