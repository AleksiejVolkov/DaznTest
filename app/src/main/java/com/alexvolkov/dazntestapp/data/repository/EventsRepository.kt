package com.alexvolkov.dazntestapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexvolkov.dazntestapp.data.api.EventsApi
import com.alexvolkov.dazntestapp.data.entity.Event
import kotlinx.coroutines.flow.Flow

class EventsRepository(
    private val apiService: EventsApi
) {
    private val pager = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { EventsPagingSource(apiService) }
    )

    fun getEventsStream(): Flow<PagingData<Event>> {
        return pager.flow
    }
}

class EventsPagingSource(
    private val apiService: EventsApi,
) : PagingSource<Int, Event>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        return try {
            val events = apiService.getAllEvents()
            val page = params.key ?: 1
            val fromIndex = (page - 1) * params.loadSize
            val toIndex = kotlin.math.min(fromIndex + params.loadSize, events.size)

            LoadResult.Page(
                data = events.sortedBy { it.date },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (toIndex == events.size) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}