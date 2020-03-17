package com.hotmail.leon.zimmermann.homeassistant.ui.components.picker

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.number_picker.view.*

class NumberPickerDialog(val value: Int, val min: Int = 0, val max: Int = 10_000, val callback: (Int) -> Unit) :
    DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(context)
            val view = requireActivity().layoutInflater.inflate(R.layout.number_picker, null)
            view.number_picker_input.let {
                it.minValue = min
                it.maxValue = max
                it.value = value
            }
            builder.setTitle(R.string.number_picker)
            builder.setView(view)
            builder.setPositiveButton(R.string.submit) { _, _ -> callback(view.number_picker_input.value) }
            builder.setNegativeButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.cancel() }
            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }
}