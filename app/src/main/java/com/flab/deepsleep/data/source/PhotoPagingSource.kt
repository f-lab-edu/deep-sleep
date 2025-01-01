package com.flab.deepsleep.data.source

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
private val firstArticleCreatedTime = LocalDateTime.now()

class PhotoPagingSource: PagingSource<Int, SinglePhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SinglePhoto> {
        TODO("Not yet implemented")
    }

    override fun getRefreshKey(state: PagingState<Int, SinglePhoto>): Int? {
        TODO("Not yet implemented")
    }

}