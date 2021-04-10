package de.usedup.android.application

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UsedupApplication: Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}