package com.styleme.app

import android.app.Application
import com.styleme.app.utils.CloudinaryManager
import timber.log.Timber

class StyleMeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        CloudinaryManager.init(this)
    }
}
