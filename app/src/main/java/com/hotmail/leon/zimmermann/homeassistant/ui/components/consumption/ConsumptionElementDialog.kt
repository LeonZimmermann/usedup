package com.hotmail.leon.zimmermann.homeassistant.ui.components.consumption

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.Value
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.editor.MealEditorViewModel
import kotlinx.android.synthetic.main.meal_ingredients_dialog.view.*
import java.io.Serializable
import java.lang.RuntimeException

class ConsumptionElementDialog(callback: ((ConsumptionElement) -> Unit)? = null) : DialogFragment() {

    private lateinit var viewModel: ConsumptionElementViewModel
    private var callback: Callback = object : Callback {
        override fun call(value: ConsumptionElement) {
            callback?.let { it(value) } ?: throw RuntimeException("No callback supplied!")
        }
    }

    private interface Callback : Serializable {
        fun call(value: ConsumptionElement)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ConsumptionElementViewModel::class.java)
        savedInstanceState?.apply {
            callback = getSerializable(CALLBACK) as Callback
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = requireActivity().layoutInflater.inflate(R.layout.meal_ingredients_dialog, null)
            view.name_input.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.list_item,
                    viewModel.products.map { it.second.name }
                )
            )
            view.measure_input.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.list_item,
                    MeasureRepository.measures.map { it.second.name })
            )
            MaterialAlertDialogBuilder(context)
                .setView(view)
                .setPositiveButton(R.string.submit) { _, _ ->
                    val product = viewModel.products.map { it.second }
                        .first { it.name == view.name_input.text.toString() }
                    val measure = MeasureRepository.getMeasureForName(view.measure_input.text.toString())
                    val consumption = view.consumption_input.text.toString().toDouble()
                    callback.call(ConsumptionElement(product, Value(consumption, measure)))
                }
                .setNegativeButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.cancel() }
                .create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(CALLBACK, callback)
    }

    companion object {
        private const val CALLBACK = "Callback"
    }
}