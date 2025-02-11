package com.flab.deepsleep.ui.main

import android.app.Application
import com.flab.deepsleep.BuildConfig
import com.flab.deepsleep.data.entity.photo.PhotoDatabase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    val database: PhotoDatabase by lazy { PhotoDatabase.getDatabase(this) }

    class App : Application() {
        override fun onCreate() {
            super.onCreate()

            if (BuildConfig.DEBUG) {
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
}