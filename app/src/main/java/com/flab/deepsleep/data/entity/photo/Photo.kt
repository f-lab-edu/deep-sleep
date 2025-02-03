package com.flab.deepsleep.data.entity.photo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flab.deepsleep.data.entity.room.UiItem

@Entity(tableName = "photo")
data class Photo(
    @PrimaryKey(autoGenerate = true) val pk: Int = 0,
    @ColumnInfo(name = "id") val id: String?,
    @ColumnInfo(name = "likes") val likes: Int,
    @ColumnInfo(name = "urls") val urls: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "created_at") val createdAt: String?,
    @ColumnInfo(name = "username") val username: String?,
    @ColumnInfo(name = "is_like", defaultValue = "0") val isLike: Boolean = false
)

fun Photo.toUiItem(): UiItem {
    return UiItem(
        id = this.id,
        likes = this.likes,
        urls = this.urls,
        createdAt = this.createdAt,
        description = this.description,
        username = this.username,
        isLike = true,
    )
}