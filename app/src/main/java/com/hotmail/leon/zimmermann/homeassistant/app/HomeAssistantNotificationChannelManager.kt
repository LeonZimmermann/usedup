package com.hotmail.leon.zimmermann.homeassistant.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeAssistantNotificationChannelManager private constructor(private val context: Context) {

  private val notificationManager: NotificationManager =
    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

  suspend fun createNotificationChannels() = withContext(Dispatchers.Default) {
    createShoppingNotificationChannel()
  }

  private fun createShoppingNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val name = context.getString(R.string.shopping_notification_channel_name)
      val descriptionText = context.getString(R.string.shopping_notification_channel_description)
      val importance = NotificationManager.IMPORTANCE_DEFAULT
      val channel =
        NotificationChannel(SHOPPING_CHANNEL_ID, name, importance).apply {
          description = descriptionText
        }
      notificationManager.createNotificationChannel(channel)
    }
  }

  companion object {
    const val SHOPPING_CHANNEL_ID = "ShoppingChannelId"

    private var instance: HomeAssistantNotificationChannelManager? = null
    fun getInstance(context: Context): HomeAssistantNotificationChannelManager {
      if (instance == null) instance = HomeAssistantNotificationChannelManager(context)
      return instance!!
    }
  }
}