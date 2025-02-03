package com.flab.deepsleep.data.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(
    @PrimaryKey val repoId: String, val prevKey: Int?, val nextKey: Int?
)