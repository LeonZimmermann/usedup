package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.editor

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.MealIngredient
import com.hotmail.leon.zimmermann.homeassistant.datamodel.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.Value
import kotlinx.android.synthetic.main.meal_ingredients_dialog.view.*

class MealIngredientsDialog : DialogFragment() {

    private lateinit var viewModel: MealEditorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MealEditorViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = requireActivity().layoutInflater.inflate(R.layout.meal_ingredients_dialog, null)
            viewModel.products.observe(this, Observer { products ->
                view.name_input.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        R.layout.list_item,
                        products.map { it.second.name }
                    )
                )
            })
            view.measure_input.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.list_item,
                    MeasureRepository.measures.map { it.second.name })
            )

            MaterialAlertDialogBuilder(context)
                .setView(view)
                .setPositiveButton(R.string.submit) { _, _ ->
                    val product = viewModel.products.value!!.map { it.second }
                        .first { it.name == view.name_input.text.toString() }
                    val measure = MeasureRepository.getMeasureForName(view.measure_input.text.toString())
                    val consumption = view.consumption_input.text.toString().toDouble()
                    viewModel.addMealTemplate(MealTemplate(product, Value(consumption, measure)))
                }
                .setNegativeButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.cancel() }
                .create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }
}