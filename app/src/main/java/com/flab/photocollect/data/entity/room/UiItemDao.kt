package com.flab.photocollect.data.entity.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UiItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(model: List<UiItem>)

    @Query("SELECT * FROM uiItems")
    fun getAllUiItem(): PagingSource<Int, UiItem>

    @Query("DELETE FROM uiItems")
    suspend fun clearAllUiItem()
}