package com.m7md7sn.dentel.app.android

import android.app.Application
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp
import java.util.HashMap

@HiltAndroidApp
class DentelApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = HashMap<String, String>()
        config["cloud_name"] = "dn75n9k4u"
        MediaManager.init(this, config)
    }
}