package com.hotmail.leon.zimmermann.homeassistant.components.picker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.io.Serializable
import java.util.*

class DatePickerDialogFragment(private var onDateSetListener: OnDateSetListener) : DialogFragment() {

  interface OnDateSetListener : DatePickerDialog.OnDateSetListener, Serializable

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    savedInstanceState?.apply {
      onDateSetListener = getSerializable(ON_DATE_SET_LISTENER) as OnDateSetListener
    }
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    return DatePickerDialog(requireContext(), onDateSetListener, year, month, day)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putSerializable(ON_DATE_SET_LISTENER, onDateSetListener)
  }

  companion object {
    private const val ON_DATE_SET_LISTENER = "DatePickerDialog#OnDateSetListener"
  }
}