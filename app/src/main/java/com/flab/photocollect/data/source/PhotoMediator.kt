package com.flab.photocollect.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.flab.photocollect.data.entity.room.AppDatabase
import com.flab.photocollect.data.entity.room.RemoteKeys
import com.flab.photocollect.data.entity.room.UiItem
import com.flab.photocollect.data.entity.unplash.toUiItem
import com.flab.photocollect.data.repository.photo.PagingRepository
import com.flab.photocollect.data.repository.photo.UnsplashRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PhotoMediator(
    private val query: String,
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
            val response = if (query.isEmpty()) {
                unsplashRepository.getListPhotos(page, state.config.pageSize).map {
                    it.toUiItem()
                }
            } else {
                getSearchPhotos(query, page, state.config.pageSize)
            }
            val isEndOfList = response.isEmpty()
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.getRepoDao().clearRemoteKeys()
                    appDatabase.getUiItemDao().clearAllUiItem()
                }
                val prevKey = if (page == PagingRepository.DEFAULT_PAGE_INDEX) null else page - 1
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
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getSearchPhotos(query: String, page: Int, perPage: Int): List<UiItem> {
        return coroutineScope {
            try {
                unsplashRepository.getSearchPhotos(query, page, perPage)
                    .results
                    ?.mapNotNull { result ->
                        async {
                            result?.takeIf { it.description != null }?.id?.let { photoId ->
                                runCatching {
                                    unsplashRepository.getSinglePhotoById(photoId).toUiItem()
                                }.getOrNull()
                            }
                        }
                    }?.awaitAll()?.filterNotNull() ?: emptyList()
            } catch (e: IOException) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, UiItem>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: PagingRepository.DEFAULT_PAGE_INDEX
            }

            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                if (remoteKeys?.nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.nextKey
            }

            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                if (remoteKeys?.prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.prevKey
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