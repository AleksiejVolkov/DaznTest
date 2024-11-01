package com.alexvolkov.dazntestapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexvolkov.dazntestapp.data.api.RetrofitInstance
import com.alexvolkov.dazntestapp.domain.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EventsRepository {

    private var allEvents: List<Event> = emptyList()
    private var pager: Pager<Int, Event>? = null

    suspend fun fetchAllEvents() {
        allEvents = RetrofitInstance.api.getAllEvents()
        pager = Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { EventsPagingSource(allEvents) }
        )
    }

    fun getEventsStream(): Flow<PagingData<Event>> {
        return pager?.flow ?: flowOf()
    }
}

class EventsPagingSource(private val allEvents: List<Event>) : PagingSource<Int, Event>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        println("HUI load")
        val page = params.key ?: 1
        val fromIndex = (page - 1) * params.loadSize
        val toIndex = kotlin.math.min(fromIndex + params.loadSize, allEvents.size)

        return if (fromIndex < allEvents.size) {
            LoadResult.Page(
                data = allEvents.subList(fromIndex, toIndex),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (toIndex == allEvents.size) null else page + 1
            )
        } else {
            LoadResult.Error(Exception("Invalid page index"))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}