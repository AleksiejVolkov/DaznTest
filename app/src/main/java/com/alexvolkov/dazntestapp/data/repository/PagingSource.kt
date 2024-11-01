package com.alexvolkov.dazntestapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexvolkov.dazntestapp.data.entity.Event

class PagingSource(private val allEvents: List<Event>) : PagingSource<Int, Event>() {
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