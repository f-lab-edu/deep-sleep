package com.flab.deepsleep.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import com.flab.deepsleep.data.source.PhotoPagingSource

class PhotoRepository {
    fun getPhotos(): Pager<Int, SinglePhoto> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { PhotoPagingSource() }
        )
    }
}