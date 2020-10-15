package com.hotmail.leon.zimmermann.homeassistant.app.settings

import android.content.Context
import android.widget.TimePicker
import androidx.fragment.app.FragmentManager
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.MultiSelectListPreference
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.app.enumerationJoin
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.notification.ShoppingNotificationManager
import com.hotmail.leon.zimmermann.homeassistant.app.toDisplayString
import com.hotmail.leon.zimmermann.homeassistant.components.picker.TimePickerDialogFragment
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek

class HomeAssistantPreferenceViewModel @ViewModelInject constructor(@ApplicationContext private val context: Context) :
  ViewModel() {

  private val shoppingNotificationManager = ShoppingNotificationManager(context)
  val shoppingWeekdayPreferenceEntryValues: MutableLiveData<Array<CharSequence>> = MutableLiveData()
  val shoppingWeekdayPreferenceEntries: MutableLiveData<Array<CharSequence>> = MutableLiveData()
  val shoppingWeekdayPreferenceSummary: MutableLiveData<String> = MutableLiveData()

  val dinnerTimePreferenceText: MutableLiveData<String> = MutableLiveData()

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
      "$summaryEntries selected"
    } else context.resources.getString(R.string.shopping_notification_channel_description))
  }

  fun onDinnerTimePreferenceClicked(fragmentManager: FragmentManager): Boolean {
    TimePickerDialogFragment(object : TimePickerDialogFragment.OnTimeSetListener {
      override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        dinnerTimePreferenceText.postValue("$hourOfDay:$minute")
      }
    }).show(fragmentManager, "TIME_PICKER_FRAGMENT")
    return true
  }
}