package com.flab.photocollect.data.repository.photo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.flab.photocollect.data.entity.room.AppDatabase
import com.flab.photocollect.data.entity.room.UiItem
import com.flab.photocollect.data.source.PhotoMediator
import kotlinx.coroutines.flow.Flow

class PagingRepository(
    private val appDatabase: AppDatabase,
    private val unsplashRepository: UnsplashRepository
) {
    companion object {
        const val DEFAULT_PAGE_INDEX = 1
        const val DEFAULT_PAGE_SIZE = 20
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = false)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun letPagingImagesFlowDb(query: String, pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<UiItem>> {
        val pagingSourceFactory = { appDatabase.getUiItemDao().getAllUiItem() }
        return Pager(
            config = pagingConfig,
            remoteMediator = PhotoMediator(query, appDatabase, unsplashRepository),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


}