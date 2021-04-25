package de.usedup.android.preferences

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.MultiSelectListPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.usedup.android.R
import de.usedup.android.shopping.notification.ShoppingNotificationManager
import de.usedup.android.utils.enumerationJoin
import de.usedup.android.utils.toDisplayString
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class UsedupPreferenceViewModel @Inject constructor(@ApplicationContext private val context: Context) :
  ViewModel() {

  private val shoppingNotificationManager = ShoppingNotificationManager(context)
  val shoppingWeekdayPreferenceEntryValues: MutableLiveData<Array<CharSequence>> = MutableLiveData()
  val shoppingWeekdayPreferenceEntries: MutableLiveData<Array<CharSequence>> = MutableLiveData()
  val shoppingWeekdayPreferenceSummary: MutableLiveData<String> = MutableLiveData()

  fun initShoppingPreference(shoppingWeekdayPreference: MultiSelectListPreference) {
    shoppingWeekdayPreferenceEntryValues.postValue(DayOfWeek.values().map { it.value.toString() }.toTypedArray())
    shoppingWeekdayPreferenceEntries.postValue(DayOfWeek.values().map { it.toDisplayString() }.toTypedArray())
    setSummaryForShoppingPreference(shoppingWeekdayPreference.values.map { DayOfWeek.of(it.toInt()) }.toSet())
  }

  fun updateShoppingWeekdays(newValue: HashSet<String>) {
    val weekdays = newValue.map { DayOfWeek.of(it.toInt()) }.toSet()
    setSummaryForShoppingPreference(weekdays)
    if (weekdays.isNotEmpty()) shoppingNotificationManager.dispatchShoppingNotifications(weekdays)
    else shoppingNotificationManager.disableShoppingNotifications()
  }

  private fun setSummaryForShoppingPreference(values: Set<DayOfWeek>) {
    shoppingWeekdayPreferenceSummary.postValue(if (values.isNotEmpty()) {
      val summaryEntries = values
        .map { it.toDisplayString() }
        .enumerationJoin(context)
      context.resources.getString(R.string.selected_shopping_days, summaryEntries)
    } else context.resources.getString(R.string.shopping_notification_channel_description))
  }

  fun updateDinnerNotificationSchedule(time: TimePreference.Time) {
    TODO("not implemented")
  }

}