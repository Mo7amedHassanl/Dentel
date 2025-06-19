package com.m7md7sn.dentel.app.android

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.cloudinary.android.MediaManager
import com.m7md7sn.dentel.utils.LocaleUtils
import dagger.hilt.android.HiltAndroidApp
import java.util.HashMap

@HiltAndroidApp
class DentelApplication : Application() {
    override fun attachBaseContext(base: Context) {
        // Apply saved language settings before attaching base context
        super.attachBaseContext(LocaleUtils.applyLocale(base))
    }

    override fun onCreate() {
        super.onCreate()
        val config = HashMap<String, String>()
        config["cloud_name"] = "dn75n9k4u"
        MediaManager.init(this, config)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Re-apply locale on configuration changes
        LocaleUtils.applyLocale(this)
    }
}