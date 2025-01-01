package com.flab.deepsleep.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flab.deepsleep.BuildConfig
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import com.flab.deepsleep.data.repo.UnplashRepositoryImpl

class PhotoPagingSource(
    private val unplashRepositoryImpl: UnplashRepositoryImpl,
    private val photoIdList: List<String>
) : PagingSource<Int, SinglePhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SinglePhoto> {
        return try {
            // 현재 페이지 키 가져오기
            val currentPage = params.key ?: 0
            val start = currentPage * params.loadSize
            val end = minOf(start + params.loadSize, photoIdList.size)

            // ID 리스트에서 현재 페이지 범위에 해당하는 IDs 가져오기
            val currentIds = photoIdList.subList(start, end)
            val clientId = BuildConfig.UNSPLASH_ACCESS_KEY

            // 각각의 ID로 API 호출하여 사진 데이터를 가져옴
            val photos = currentIds.mapNotNull { id ->
                try {
                    unplashRepositoryImpl.getSinglePhotoById(id) // 개별 API 호출
                } catch (e: Exception) {
                    null // 호출 실패 시 null 반환
                }
            }

            LoadResult.Page(
                data = photos,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (end >= photoIdList.size) null else currentPage + 1
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