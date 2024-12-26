package com.flab.deepsleep

import android.app.Application
import com.flab.deepsleep.utils.TimberDebugTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.*


@HiltAndroidApp
class MainApplication: Application(){

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(TimberDebugTree())
        }
    }
}

class MainApplication: Application()

