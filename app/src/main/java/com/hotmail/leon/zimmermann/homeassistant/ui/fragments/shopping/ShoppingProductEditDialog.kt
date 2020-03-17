package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.shopping_product_edit_dialog.view.*

class ShoppingProductEditDialog(
    private val value: Int,
    private val editCallback: (Int) -> Unit,
    private val deleteCallback: () -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(context)
            val view = requireActivity().layoutInflater.inflate(R.layout.shopping_product_edit_dialog, null)
            view.number_picker_input.let {
                it.minValue = 0
                it.maxValue = 20
                it.value = value
            }
            builder.setTitle(R.string.number_picker)
            builder.setView(view)
            builder.setPositiveButton(R.string.submit) { _, _ -> editCallback(view.number_picker_input.value) }
            builder.setNeutralButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.cancel() }
            builder.setNegativeButton(R.string.delete) { _, _ -> deleteCallback() }
            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }
}