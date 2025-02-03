package com.flab.deepsleep.data.repository.photo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.flab.deepsleep.data.entity.room.AppDatabase
import com.flab.deepsleep.data.entity.room.UiItem
import com.flab.deepsleep.data.source.PhotoMediator
import kotlinx.coroutines.flow.Flow

class PagingRepository(
    private val appDatabase: AppDatabase,
    private val unsplashRepository: UnsplashRepository
) {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = false)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun letPagingImagesFlowDb(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<UiItem>> {
        val pagingSourceFactory = { appDatabase.getUiItemDao().getAllUiItem() }
        return Pager(
            config = pagingConfig,
            remoteMediator = PhotoMediator(appDatabase, unsplashRepository),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}