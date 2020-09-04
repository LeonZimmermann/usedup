package com.hotmail.leon.zimmermann.homeassistant

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class HomeAssistantApplication: Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}