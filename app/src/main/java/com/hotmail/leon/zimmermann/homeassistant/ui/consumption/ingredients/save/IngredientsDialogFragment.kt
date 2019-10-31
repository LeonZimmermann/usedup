package com.hotmail.leon.zimmermann.homeassistant.ui.consumption.ingredients.save

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.consumption_creation_dialog.view.*
import java.io.Serializable

class IngredientsDialogFragment(): DialogFragment() {

    @FunctionalInterface
    interface OnSaveHandler: Serializable {
        fun onSave(name: String)
    }

    private lateinit var viewModel: IngredientsDialogViewModel
    private lateinit var onSaveHandler: OnSaveHandler

    constructor(onSaveHandler: OnSaveHandler): this() {
        this.onSaveHandler = onSaveHandler
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this)[IngredientsDialogViewModel::class.java]
        if (savedInstanceState != null) {
            onSaveHandler = savedInstanceState.getSerializable(ON_SAVE_HANDLER_KEY) as OnSaveHandler
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = requireActivity().layoutInflater.inflate(R.layout.consumption_creation_dialog, null)
            viewModel.name.observe(this, Observer { name ->
                view.save_consumption_dialog_name_input.setText(name)
            })
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.save_consumption_list)
            builder.setView(view)
            builder.setPositiveButton(R.string.submit) { dialogInterface: DialogInterface, i: Int ->
                onSaveHandler.onSave(view.save_consumption_dialog_name_input.text.toString())
            }
            builder.setNegativeButton(R.string.cancel) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.cancel()
            }
            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(ON_SAVE_HANDLER_KEY, onSaveHandler)
    }

    companion object {
        private const val ON_SAVE_HANDLER_KEY = "onSaveHandler"
    }
}