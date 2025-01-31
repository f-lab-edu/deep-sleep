package com.flab.deepsleep.data.entity.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Query("SELECT * FROM `photo` order by pk DESC")
    fun getAll(): Flow<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg like: Photo)

    @Query("DELETE FROM photo WHERE id = :id")
    suspend fun delete(id: String)
}