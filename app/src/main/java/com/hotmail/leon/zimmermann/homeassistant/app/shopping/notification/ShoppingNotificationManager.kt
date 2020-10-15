package com.hotmail.leon.zimmermann.homeassistant.app.shopping.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.DayOfWeek
import java.util.*

class ShoppingNotificationManager(private val context: Context) {

  private val alarmManager = requireNotNull(context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)

  fun dispatchShoppingNotifications(shoppingDays: Set<DayOfWeek>) {
    disableShoppingNotifications()
    val pendingIntent =
      PendingIntent.getService(context, REQUEST_ID, Intent(context, ShoppingNotificationService::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT)
    for (shoppingDay in shoppingDays) {
      val calendar = Calendar.getInstance()
      calendar[Calendar.DAY_OF_WEEK] = shoppingDay.value
      calendar[Calendar.HOUR_OF_DAY] = 15
      calendar[Calendar.MINUTE] = 0
      calendar[Calendar.MILLISECOND] = 0
      alarmManager.setRepeating(AlarmManager.RTC, calendar.timeInMillis, AlarmManager.INTERVAL_DAY * 7,
        pendingIntent)
    }
  }

  fun disableShoppingNotifications() {
    val pendingIntent =
      PendingIntent.getService(context, REQUEST_ID, Intent(context, ShoppingNotificationService::class.java),
        PendingIntent.FLAG_NO_CREATE)
    if (pendingIntent != null) alarmManager.cancel(pendingIntent)
  }

  companion object {
    private const val REQUEST_ID = 1
  }
}