package com.hotmail.leon.zimmermann.homeassistant.components.picker

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.DialogFragment
import java.io.Serializable
import java.util.*

class TimePickerDialogFragment(private var onTimeSetListener: OnTimeSetListener) : DialogFragment() {

  interface OnTimeSetListener : TimePickerDialog.OnTimeSetListener, Serializable

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    savedInstanceState?.apply {
      onTimeSetListener = getSerializable(ON_Time_SET_LISTENER) as OnTimeSetListener
    }
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val calendar = Calendar.getInstance()
    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    return TimePickerDialog(requireContext(), onTimeSetListener, hourOfDay, minute, DateFormat.is24HourFormat(activity))
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putSerializable(ON_Time_SET_LISTENER, onTimeSetListener)
  }

  companion object {
    private const val ON_Time_SET_LISTENER = "TimePickerDialog#OnTimeSetListener"
  }
}