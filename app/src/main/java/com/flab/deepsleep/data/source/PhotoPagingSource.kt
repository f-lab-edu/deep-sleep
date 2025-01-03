package com.flab.deepsleep.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import com.flab.deepsleep.data.repo.UnplashRepositoryImpl
import com.flab.deepsleep.utils.Constants

class PhotoPagingSource(
    private val unplashRepositoryImpl: UnplashRepositoryImpl,
    private val query: String
) : PagingSource<Int, SinglePhoto>() {
    private val STARTING_KEY = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SinglePhoto> {
        val page = params.key ?: STARTING_KEY
        try {
            val response = unplashRepositoryImpl.getSearchPhotos(query)
            val results = response.results?.filterNotNull() ?: emptyList()

            val photos = results.mapNotNull { result ->
                val singlePhoto = result.id?.let { unplashRepositoryImpl.getSinglePhotoById(it) }
                singlePhoto?.let {
                    SinglePhoto(
                        id = singlePhoto?.id,
                        description = singlePhoto?.description,
                        color = singlePhoto?.color,
                        createdAt = singlePhoto?.createdAt,
                        downloads = 0,
                        height = 0,
                        width = 0,
                        blurHash = singlePhoto?.blurHash,
                        likedByUser = false,
                        likes = 0,
                        publicDomain = true,
                        updatedAt = singlePhoto?.updatedAt,
                        exif = singlePhoto?.exif,
                        urls = singlePhoto?.urls,
                        user = singlePhoto?.user
                    )
                }
            }
            return LoadResult.Page(
                data = photos,
                prevKey = if (page == STARTING_KEY) null else page - 1,
                nextKey = if (photos.isEmpty()) null else page + (params.loadSize / Constants.NETWORK_PAGE_SIZE)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SinglePhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}