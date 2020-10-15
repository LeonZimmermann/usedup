package com.hotmail.leon.zimmermann.homeassistant.app.shopping.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.runBlocking

class ShoppingNotificationService : Service(), CoroutineScope by MainScope() {

  override fun onBind(intent: Intent?): IBinder? = null

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    runBlocking {
      ShoppingNotificationBuilder.showShoppingNotification(applicationContext)
      stopSelf()
    }
    return super.onStartCommand(intent, flags, startId)
  }
}