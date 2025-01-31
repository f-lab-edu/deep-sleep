package com.flab.deepsleep

import android.app.Application
import com.flab.deepsleep.data.entity.room.AppDatabase
import com.flab.deepsleep.utils.TimberDebugTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class MainApplication: Application(){
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            // Timber Initialize
            Timber.uprootAll()
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    val threadName = Thread.currentThread().name
                    return "<$threadName> (${element.fileName}:${element.lineNumber})#${element.methodName} "
                }
            })

        }
    }
}

