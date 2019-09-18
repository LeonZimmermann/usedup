package com.hotmail.leon.zimmermann.homeassistant.management.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.hotmail.leon.zimmermann.homeassistant.R

class ManagementItemDialog(private val mode: ProductItemDialogMode) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = requireActivity().layoutInflater.inflate(R.layout.management_item_dialog, null)
            val builder = AlertDialog.Builder(it)
                .setTitle("Product")
                .setView(view)
                .setNeutralButton(R.string.cancel) { dialogInterface, i -> dialogInterface.cancel() }
            mode.apply(builder, view)
            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }
}