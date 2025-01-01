package com.flab.deepsleep.data.entity.search
import com.google.gson.annotations.SerializedName

data class SearchPhotos(
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("results")
    val results: List<Results?>
)

