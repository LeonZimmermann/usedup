package com.hotmail.leon.zimmermann.homeassistant.app.settings

import android.os.Bundle
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.app.enumerationJoin
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.notification.ShoppingNotificationManager
import com.hotmail.leon.zimmermann.homeassistant.app.toDisplayString
import java.time.DayOfWeek

class HomeAssistantSettingsFragment : PreferenceFragmentCompat() {

  private lateinit var shoppingNotificationManager: ShoppingNotificationManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    shoppingNotificationManager = ShoppingNotificationManager(requireContext())
  }

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preferences, rootKey)
    initShoppingWeekdaysPreferences()
    findPreference<Preference>("dinner_time")
  }

  private fun initShoppingWeekdaysPreferences() {
    requireNotNull(findPreference<MultiSelectListPreference>("shopping_weekdays")).apply {
      entryValues = DayOfWeek.values().map { it.value.toString() }.toTypedArray()
      entries = DayOfWeek.values().map { it.toDisplayString() }.toTypedArray()
      setSummaryForShoppingWeekdays(this, values.map { DayOfWeek.of(it.toInt()) }.toSet())
      setOnPreferenceChangeListener { preference, newValue ->
        updateShoppingWeekdays(preference as MultiSelectListPreference, newValue as HashSet<String>)
        true
      }
    }
  }

  private fun updateShoppingWeekdays(preference: MultiSelectListPreference, newValue: HashSet<String>) {
    val weekdays = newValue.map { DayOfWeek.of(it.toInt()) }.toSet()
    setSummaryForShoppingWeekdays(preference, weekdays)
    if (weekdays.isNotEmpty()) shoppingNotificationManager.dispatchShoppingNotifications(weekdays)
    else shoppingNotificationManager.disableShoppingNotifications()
  }

  private fun setSummaryForShoppingWeekdays(preference: MultiSelectListPreference, values: Set<DayOfWeek>) {
    preference.summary = if (values.isNotEmpty()) {
      val summaryEntries = values
        .map { it.toDisplayString() }
        .enumerationJoin(requireContext())
      "$summaryEntries selected"
    } else requireContext().resources.getString(R.string.shopping_notification_channel_description)
  }
}