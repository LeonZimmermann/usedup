package com.hotmail.leon.zimmermann.homeassistant.app.shopping

import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.Navigation
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.app.HomeAssistantNotificationChannelManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ShoppingNotificationBuilder {

  private const val SHOPPING_NOTIFICATION_ID = 1

  suspend fun showShoppingNotification(view: View) = withContext(Dispatchers.Default) {
    val pendingIntent = Navigation.findNavController(view)
      .createDeepLink()
      .setGraph(R.navigation.app_navigation)
      .setDestination(R.id.shopping_fragment).createPendingIntent()
    val builder = NotificationCompat.Builder(view.context, HomeAssistantNotificationChannelManager.SHOPPING_CHANNEL_ID)
      .setSmallIcon(R.drawable.cart_icon)
      .setContentTitle("Shopping")
      .setContentIntent(pendingIntent)
      .setContentText("You need to go shopping today")
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setAutoCancel(true)
    with(NotificationManagerCompat.from(view.context)) {
      notify(SHOPPING_NOTIFICATION_ID, builder.build())
    }
  }

}