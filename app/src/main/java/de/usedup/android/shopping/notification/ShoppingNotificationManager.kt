package de.usedup.android.shopping.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.DayOfWeek
import java.util.*

class ShoppingNotificationManager(private val context: Context) {

  private val alarmManager = requireNotNull(context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)
  private val requestIdStore = context.getSharedPreferences(REQUEST_ID_STORE_IDENTIFIER, 0)

  fun dispatchShoppingNotifications(shoppingDays: Set<DayOfWeek>) {
    disableShoppingNotifications()
    val storeEdit = requestIdStore.edit()
    for ((index, shoppingDay) in shoppingDays.withIndex()) {
      val requestId = BASE_REQUEST_ID + index
      val pendingIntent =
        PendingIntent.getService(context, requestId, Intent(context, ShoppingNotificationService::class.java),
          PendingIntent.FLAG_CANCEL_CURRENT)
      storeEdit.putInt(requestId.toString(), requestId)
      alarmManager.setRepeating(AlarmManager.RTC, getLongForShoppingDay(shoppingDay, index), ONE_WEEK_INTERVAL,
        pendingIntent)
    }
    storeEdit.apply()
  }

  private fun getLongForShoppingDay(shoppingDay: DayOfWeek, offset: Int): Long {
    val calendar = Calendar.getInstance()
    calendar[Calendar.DAY_OF_WEEK] = shoppingDay.value
    calendar[Calendar.HOUR_OF_DAY] = 9
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.MILLISECOND] = 0
    return calendar.timeInMillis
  }

  fun disableShoppingNotifications() {
    requestIdStore.all.forEach { (idAsString, _) ->
      val requestId = idAsString.toInt()
      val pendingIntent =
        PendingIntent.getService(context, requestId, Intent(context, ShoppingNotificationService::class.java),
          PendingIntent.FLAG_UPDATE_CURRENT)
      if (pendingIntent != null) alarmManager.cancel(pendingIntent)
    }
    requestIdStore.edit()
      .clear()
      .apply()
  }

  companion object {
    private const val BASE_REQUEST_ID = 0x0
    private const val REQUEST_ID_STORE_IDENTIFIER = "shopping_notification_request_ids"
    private const val ONE_WEEK_INTERVAL = AlarmManager.INTERVAL_DAY * 7
  }
}