package de.usedup.android.preferences

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.core.os.bundleOf
import androidx.preference.PreferenceDialogFragmentCompat
import java.io.Serializable
import java.util.*

class TimePreferenceDialogFragment(private var onTimeSetListener: OnTimeSetListener) :
  PreferenceDialogFragmentCompat() {

  interface OnTimeSetListener : TimePickerDialog.OnTimeSetListener, Serializable

  private lateinit var time: TimePreference.Time

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    savedInstanceState?.apply {
      onTimeSetListener = getSerializable(ON_TIME_SET_LISTENER) as OnTimeSetListener
      time = getSerializable(TIME) as TimePreference.Time
    }
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val calendar = Calendar.getInstance()
    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    return TimePickerDialog(requireContext(), onTimeSetListener, hourOfDay, minute, DateFormat.is24HourFormat(activity))
  }

  override fun onDialogClosed(positiveResult: Boolean) {

  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putSerializable(ON_TIME_SET_LISTENER, onTimeSetListener)
    outState.putSerializable(TIME, time)
  }

  companion object {
    private const val ON_TIME_SET_LISTENER = "TimePickerDialog#OnTimeSetListener"
    private const val TIME = "TimePreference#Time"

    fun newInstance(key: String, onTimeSetListener: OnTimeSetListener): TimePreferenceDialogFragment {
      return TimePreferenceDialogFragment(onTimeSetListener).apply {
        arguments = bundleOf(ARG_KEY to key)
      }
    }
  }
}