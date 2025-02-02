package com.flab.deepsleep.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flab.deepsleep.data.entity.unplash.SinglePhoto

class PhotoPagingSource(
    private val query: String,
    private val getSearchPhotos: suspend (String, Int) -> List<SinglePhoto>
) : PagingSource<Int, SinglePhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SinglePhoto> {
        val page = params.key ?: 1
        return try {
            val photos = getSearchPhotos(query, page)
            LoadResult.Page(
                data = photos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (photos.isNotEmpty()) page + 1 else null
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