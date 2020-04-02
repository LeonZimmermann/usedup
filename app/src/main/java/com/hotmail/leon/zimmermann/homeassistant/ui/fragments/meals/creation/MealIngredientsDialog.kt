package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.creation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import kotlinx.android.synthetic.main.dinner_ingredients_dialog.view.*

class MealIngredientsDialog : DialogFragment() {

    private lateinit var viewModel: MealCreationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MealCreationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = requireActivity().layoutInflater.inflate(R.layout.dinner_ingredients_dialog, null)
            viewModel.productList.observe(this, Observer { productList ->
                view.name_input.setAdapter(
                    ArrayAdapter(
                        context!!,
                        android.R.layout.simple_list_item_1,
                        productList.map { it.name }
                    )
                )
            })
            view.measure_input.adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, Measure.values())
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Add Ingredients")
            builder.setView(view)
            builder.setPositiveButton(R.string.submit) { _, _ ->
                // TODO Add appropriate error handling
                val product =
                    viewModel.productList.value!!.firstOrNull { it.name == view.name_input.text.toString() }
                        ?: throw Exception("Product not found!")
                val measure = view.measure_input.selectedItem as Measure
                val value = view.quantity_change_input.text.toString().toDouble()
                viewModel.addConsumptionTemplate(MealTemplate(product, measure, value))
            }
            builder.setNegativeButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.cancel() }
            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }
}