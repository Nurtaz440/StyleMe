package com.styleme.app

import android.app.Application
import com.styleme.app.utils.CloudinaryManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class StyleMeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        CloudinaryManager.init(this)
        // Keep Render server warm - ping every 10 minutes
        CoroutineScope(Dispatchers.IO).launch {
            while(true) {
                try {
                    okhttp3.OkHttpClient().newCall(
                        okhttp3.Request.Builder()
                            .url("https://styleme-api.onrender.com/health")
                            .build()
                    ).execute()
                } catch (e: Exception) { }
                delay(10 * 60 * 1000) // 10 minutes
            }
        }
    }
}
