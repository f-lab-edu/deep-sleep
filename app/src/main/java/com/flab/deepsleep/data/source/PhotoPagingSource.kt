package com.flab.deepsleep.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import timber.log.Timber

class PhotoPagingSource(
    private val photos: List<SinglePhoto>
) : PagingSource<Int, SinglePhoto>() {
    private val STARTING_KEY = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SinglePhoto> {
        val page = params.key ?: 1
        val start = (page - 1) * params.loadSize
        val end = minOf(start + params.loadSize, photos.size)
        return try {
            LoadResult.Page(
                data = photos.subList(start, end),
                prevKey = if (page == STARTING_KEY) null else page - 1,
                nextKey = if (photos.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SinglePhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}