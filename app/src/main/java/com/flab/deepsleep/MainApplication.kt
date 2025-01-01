package com.flab.deepsleep

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class MainApplication: Application(){
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

