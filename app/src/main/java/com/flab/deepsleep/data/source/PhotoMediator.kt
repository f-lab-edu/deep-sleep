package com.flab.deepsleep.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.flab.deepsleep.data.entity.room.AppDatabase
import com.flab.deepsleep.data.entity.room.RemoteKeys
import com.flab.deepsleep.data.entity.room.UiItem
import com.flab.deepsleep.data.entity.unplash.toUiItem
import com.flab.deepsleep.data.repository.photo.PagingRepository
import com.flab.deepsleep.data.repository.photo.UnsplashRepository
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class PhotoMediator(
    private val appDatabase: AppDatabase,
    private val unsplashRepository: UnsplashRepository
) :
    RemoteMediator<Int, UiItem>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, UiItem>): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }

            else -> {
                pageKeyData as Int
            }
        }
        try {
            val response = unsplashRepository.getListPhotos(page, state.config.pageSize).map {
                it.toUiItem()
            }
            val isEndOfList = response.isEmpty()
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.getRepoDao().clearRemoteKeys()
                    appDatabase.getUiItemDao().clearAllUiItem()
                }
                val prevKey = if (page == PagingRepository.DEFAULT_PAGE_SIZE) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.map {
                    RemoteKeys(
                        repoId = it.id ?: "",
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                appDatabase.getRepoDao().insertAll(keys)
                appDatabase.getUiItemDao().insertAll(response)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, UiItem>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: PagingRepository.DEFAULT_PAGE_SIZE
            }

            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey
            }

            LoadType.PREPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                if (remoteKeys?.nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.nextKey
            }
        }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, UiItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDatabase.getRepoDao().remoteKeysUiIemId(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, UiItem>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { uiItem -> uiItem.id?.let { appDatabase.getRepoDao().remoteKeysUiIemId(it) } }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, UiItem>): RemoteKeys? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { uiItem -> uiItem.id?.let { appDatabase.getRepoDao().remoteKeysUiIemId(it) } }
    }

}