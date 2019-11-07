package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.ingredients.dialog

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureConversionException
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.ingredients.ConsumptionIngredientsException
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.ingredients.fragment.ConsumptionIngredientsTemplate
import kotlinx.android.synthetic.main.consumption_ingredients_dialog.view.*

class ConsumptionIngredientsEditDialogFragment private constructor() :
    ConsumptionIngredientsDialogFragment("Edit Product") {
    override fun initializeButtons(builder: AlertDialog.Builder, view: View) {
        builder.setPositiveButton(R.string.submit) { dialogInterface, i ->
            onSubmitButtonClicked()
        }
        builder.setNegativeButton(R.string.delete) { dialogInterface, i ->
            onDeleteButtonClicked()
        }
        builder.setNeutralButton(R.string.cancel) { dialogInterface, i -> dialogInterface.cancel() }
    }

    private lateinit var selectedItem: ConsumptionIngredientsTemplate

    private constructor(selectedItem: ConsumptionIngredientsTemplate) : this() {
        this.selectedItem = selectedItem
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.apply {
            selectedItem = getSerializable(SELECTED_ITEM) as ConsumptionIngredientsTemplate
        }
    }

    override fun initializeView(view: View) {
        initializeProductNameInput(view)
        initializeQuantityChangeInput(view)
        initializeMeasureInput(view)
    }

    private fun initializeProductNameInput(view: View) {
        viewModel.productEntityList.observe(this, Observer { productList ->
            view.product_name_input.setAdapter(ArrayAdapter<String>(
                context!!,
                android.R.layout.simple_dropdown_item_1line,
                productList.map { it.name }
            ))
            view.product_name_input.setText(selectedItem.product.name)
        })
    }

    private fun initializeQuantityChangeInput(view: View) {
        view.quantity_change_input.setText(selectedItem.value.toString())
    }

    private fun initializeMeasureInput(view: View) {
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, Measure.values())
        view.measure_input.adapter = adapter
        view.measure_input.setSelection(adapter.getPosition(selectedItem.measure))
    }

    private fun onSubmitButtonClicked() {
        viewModel.productEntityList.observe(
            this@ConsumptionIngredientsEditDialogFragment,
            Observer { productEntityList ->
                try {
                    val consumption = getProductAndQuantityChange(productEntityList)
                    val editConsumptionList = viewModel.editConsumptionList.value!!
                    val existingConsumption = editConsumptionList
                        .filter { it != selectedItem }
                        .find { it.product.id == consumption.product.id }
                    if (existingConsumption != null) {
                        existingConsumption.value += consumption.measure.toMeasure(
                            consumption.value,
                            existingConsumption.measure
                        )
                    } else editConsumptionList.add(consumption)
                    editConsumptionList.remove(selectedItem)
                    viewModel.editConsumptionList.value = editConsumptionList
                } catch (e: ConsumptionIngredientsException) {
                    Toast.makeText(context!!, e.message, Toast.LENGTH_LONG).show()
                } catch (e: MeasureConversionException) {
                    Toast.makeText(context!!, e.message, Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    private fun onDeleteButtonClicked() {
        val consumptionList = viewModel.editConsumptionList.value!!
        consumptionList.remove(selectedItem)
        viewModel.editConsumptionList.value = consumptionList
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SELECTED_ITEM, selectedItem)
    }

    companion object {
        fun newInstance() =
            ConsumptionIngredientsEditDialogFragment()

        fun newInstance(selectedItem: ConsumptionIngredientsTemplate) =
            ConsumptionIngredientsEditDialogFragment(
                selectedItem
            )

        private const val SELECTED_ITEM = "selectedItem"
    }
}