package com.flab.deepsleep.data.entity.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
data class Photo(
    @PrimaryKey(autoGenerate = true) val pk: Int = 0,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "likes") val likes: Int,
    @ColumnInfo(name = "urls") val urls: String?,
    @ColumnInfo(name = "created_at") val createdAt: String?,
    @ColumnInfo(name = "username") val username: String?,
    @ColumnInfo(name = "is_like", defaultValue = "0") val isLike: Boolean = false,
)
