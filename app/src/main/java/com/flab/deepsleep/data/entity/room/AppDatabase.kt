package com.flab.deepsleep.data.entity.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [UiItem::class, RemoteKeys::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getRepoDao(): RemoteKeysDao
    abstract fun getUiItemDao(): UiItemDao

    companion object {
        val UIITEMS_DB = "uiItems.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, UIITEMS_DB)
                .build()
    }

}