package com.flab.deepsleep.data.repository.photo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import com.flab.deepsleep.data.source.PhotoPagingSource
import kotlinx.coroutines.flow.Flow

class PagingRepository {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    fun letPagingImagesFlow(
        pagingConfig: PagingConfig = getDefaultPageConfig(),
        query: String,
        getSearchPhotos: suspend (String, Int) -> List<SinglePhoto>
    ): Flow<PagingData<SinglePhoto>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PhotoPagingSource(query, getSearchPhotos) }
        ).flow
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = false)
    }
}