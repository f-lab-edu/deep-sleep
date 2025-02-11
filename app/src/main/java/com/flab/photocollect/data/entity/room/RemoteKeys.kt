package com.flab.photocollect.data.entity.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(
    @PrimaryKey val repoId: String,
    @ColumnInfo(name = "prevKey") val prevKey: Int?,
    @ColumnInfo(name = "nextKey")val nextKey: Int?
)