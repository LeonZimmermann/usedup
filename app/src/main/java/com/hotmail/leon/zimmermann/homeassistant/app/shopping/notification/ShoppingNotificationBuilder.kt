package com.hotmail.leon.zimmermann.homeassistant.app.shopping.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.app.notifications.HomeAssistantNotificationChannelManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ShoppingNotificationBuilder {

  private const val SHOPPING_NOTIFICATION_ID = 1

  suspend fun showShoppingNotification(context: Context) = withContext(Dispatchers.Default) {
    val pendingIntent = NavDeepLinkBuilder(context)
      .setGraph(R.navigation.app_navigation)
      .setDestination(R.id.shopping_list_fragment).createPendingIntent()
    val builder = NotificationCompat.Builder(context, HomeAssistantNotificationChannelManager.SHOPPING_CHANNEL_ID)
      .setSmallIcon(R.drawable.cart_icon)
      .setContentTitle("Shopping")
      .setContentIntent(pendingIntent)
      .setContentText("You need to go shopping today")
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setAutoCancel(true)
    with(NotificationManagerCompat.from(context)) {
      notify(SHOPPING_NOTIFICATION_ID, builder.build())
    }
  }

}