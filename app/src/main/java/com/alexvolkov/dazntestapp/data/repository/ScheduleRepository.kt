package com.alexvolkov.dazntestapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexvolkov.dazntestapp.data.api.ScheduleApi
import com.alexvolkov.dazntestapp.data.entity.Event
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(
    private val apiService: ScheduleApi
) {
    private val pager = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { SchedulePagingSource(apiService) }
    )

    fun getScheduleStream(): Flow<PagingData<Event>> {
        return pager.flow
    }
}

class SchedulePagingSource(
    private val apiService: ScheduleApi,
) : PagingSource<Int, Event>() {

    private var cachedEvents: List<Event> = emptyList()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        return try {
            val events = apiService.getSchedule()
            if (events.isNotEmpty()) {
                cachedEvents = events
            }

            val page = params.key ?: 1
            val fromIndex = (page - 1) * params.loadSize
            val toIndex = kotlin.math.min(fromIndex + params.loadSize, events.size)

            LoadResult.Page(
                data = cachedEvents.sortedBy { it.date },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (toIndex == events.size) null else page + 1
            )
        } catch (e: Exception) {
            if (cachedEvents.isNotEmpty()) {
                LoadResult.Page(
                    data = cachedEvents.sortedBy { it.date },
                    prevKey = null,
                    nextKey = null
                )
            } else {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}