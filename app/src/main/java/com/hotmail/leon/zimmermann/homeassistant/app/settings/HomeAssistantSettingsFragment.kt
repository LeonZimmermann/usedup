package com.hotmail.leon.zimmermann.homeassistant.app.settings

import android.os.Bundle
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.app.enumerationJoin
import com.hotmail.leon.zimmermann.homeassistant.app.toDisplayString
import java.time.DayOfWeek

class HomeAssistantSettingsFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preferences, rootKey)
    initShoppingWeekdaysPreferences()
    findPreference<Preference>("dinner_time")
  }

  private fun initShoppingWeekdaysPreferences() {
    requireNotNull(findPreference<MultiSelectListPreference>("shopping_weekdays")).apply {
      entryValues = DayOfWeek.values().map { it.ordinal.toString() }.toTypedArray()
      entries = DayOfWeek.values().map { it.toDisplayString() }.toTypedArray()
      /*
      if (values.isNotEmpty()) {
        val summaryEntries = values.map { DayOfWeek.valueOf(it) }
          .map { it.toDisplayString() }
          .enumerationJoin(requireContext())
        summary = "$summaryEntries selected"
      }
       */
    }
  }
}