package com.hotmail.leon.zimmermann.homeassistant.app.settings

import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hotmail.leon.zimmermann.homeassistant.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeAssistantPreferenceFragment : PreferenceFragmentCompat() {

  private val viewModel: HomeAssistantPreferenceViewModel by viewModels()

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preferences, rootKey)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initShoppingWeekdaysPreference()
    initDinnerPreference()
  }

  private fun initShoppingWeekdaysPreference() {
    val shoppingWeekdayPreference = requireNotNull(findPreference<MultiSelectListPreference>("shopping_weekdays"))
    viewModel.shoppingWeekdayPreferenceEntryValues.observe(viewLifecycleOwner,
      Observer { shoppingWeekdayPreferenceEntryValues ->
        shoppingWeekdayPreference.entryValues = shoppingWeekdayPreferenceEntryValues
      })
    viewModel.shoppingWeekdayPreferenceEntries.observe(viewLifecycleOwner, Observer { shoppingWeekayPreferenceEntries ->
      shoppingWeekdayPreference.entries = shoppingWeekayPreferenceEntries
    })
    viewModel.shoppingWeekdayPreferenceSummary.observe(viewLifecycleOwner,
      Observer { shoppingWeekdayPreferenceSummary ->
        shoppingWeekdayPreference.summary = shoppingWeekdayPreferenceSummary
      })
    shoppingWeekdayPreference.setOnPreferenceChangeListener { _, newValue ->
      viewModel.updateShoppingWeekdays(newValue as HashSet<String>)
      true
    }
    viewModel.initShoppingPreference(shoppingWeekdayPreference)
  }

  private fun initDinnerPreference() {
    val dinnerPreference = requireNotNull(findPreference<TimePreference>("dinner_time"))
    dinnerPreference.setOnPreferenceChangeListener { _, newValue ->
      viewModel.updateDinnerNotificationSchedule(newValue as TimePreference.Time)
      true
    }
  }

  override fun onDisplayPreferenceDialog(preference: Preference?) {
    if (parentFragmentManager.findFragmentByTag(TIME_PREFERENCE) != null) {
      return
    }
    if (preference is TimePreference) {
      val dialog = TimePreferenceDialogFragment.newInstance(preference.getKey(),
        object : TimePreferenceDialogFragment.OnTimeSetListener {
          override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            preference.setTime(TimePreference.Time(hourOfDay, minute))
          }
        })
      dialog.setTargetFragment(this, 0)
      dialog.show(parentFragmentManager, TIME_PREFERENCE)
    } else {
      super.onDisplayPreferenceDialog(preference)
    }
  }

  companion object {
    private const val TIME_PREFERENCE = "TimePreference"

  }
}