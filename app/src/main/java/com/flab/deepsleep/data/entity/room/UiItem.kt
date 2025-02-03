package com.flab.deepsleep.data.entity.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flab.deepsleep.data.entity.photo.Photo
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "uiItems")
data class UiItem(
    @PrimaryKey(autoGenerate = true) val pk: Int = 0,
    @ColumnInfo(name = "id") val id: String?,
    @ColumnInfo(name = "likes") val likes: Int,
    @ColumnInfo(name = "urls") val urls: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "created_at") val createdAt: String?,
    @ColumnInfo(name = "username") val username: String?,
    @ColumnInfo(name = "is_like", defaultValue = "0") val isLike: Boolean = false
) : Parcelable

fun UiItem.toPhoto(): Photo {
    return Photo(
        id = this.id,
        likes = this.likes,
        urls = this.urls,
        createdAt = this.createdAt,
        description = this.description,
        username = this.username,
        isLike = true,
    )

}