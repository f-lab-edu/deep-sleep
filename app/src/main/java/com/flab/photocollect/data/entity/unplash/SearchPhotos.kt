package com.flab.photocollect.data.entity.unplash
import com.flab.photocollect.data.entity.photos.Results
import com.google.gson.annotations.SerializedName

data class SearchPhotos(
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("results")
    val results: List<Results?>?
)

