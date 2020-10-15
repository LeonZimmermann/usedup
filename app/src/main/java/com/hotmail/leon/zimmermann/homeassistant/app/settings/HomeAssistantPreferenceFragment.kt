package com.hotmail.leon.zimmermann.homeassistant.app.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.preference.EditTextPreference
import androidx.preference.MultiSelectListPreference
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
    initShoppingWeekdaysPreferences()
    initDinnerTimePreference()
  }

  private fun initShoppingWeekdaysPreferences() {
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

  private fun initDinnerTimePreference() {
    val dinnerTimePreference = requireNotNull(findPreference<EditTextPreference>("dinner_time"))
    viewModel.dinnerTimePreferenceText.observe(viewLifecycleOwner, Observer { dinnerTimeText ->
      dinnerTimePreference.text = dinnerTimeText
    })
    dinnerTimePreference.setOnPreferenceClickListener { viewModel.onDinnerTimePreferenceClicked(parentFragmentManager) }
  }
}