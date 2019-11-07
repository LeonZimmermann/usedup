package com.hotmail.leon.zimmermann.homeassistant.ui.consumption.ingredients.dialog

import android.app.AlertDialog
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureConversionException
import com.hotmail.leon.zimmermann.homeassistant.ui.consumption.ingredients.ConsumptionIngredientsException
import kotlinx.android.synthetic.main.consumption_ingredients_dialog.view.*

class ConsumptionIngredientsAddDialogFragment private constructor() :
    ConsumptionIngredientsDialogFragment("Add Product") {

    override fun initializeView(view: View) {
        view.measure_input.adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, Measure.values())
        initializeProductNameInput(view)
    }

    private fun initializeProductNameInput(view: View) {
        viewModel.productEntityList.observe(this, Observer { productList ->
            view.product_name_input.setAdapter(ArrayAdapter<String>(
                context!!,
                android.R.layout.simple_dropdown_item_1line,
                productList.map { it.name }
            ))
        })
    }

    override fun initializeButtons(builder: AlertDialog.Builder, view: View) {
        builder.setPositiveButton(R.string.submit) { dialogInterface, i ->
            onSubmitButtonClicked()
        }
        builder.setNeutralButton(R.string.cancel) { dialogInterface, i -> dialogInterface.cancel() }
    }

    private fun onSubmitButtonClicked() {
        viewModel.productEntityList.observe(
            this@ConsumptionIngredientsAddDialogFragment,
            Observer { productEntityList ->
                try {
                    val consumption = getProductAndQuantityChange(productEntityList)
                    val consumptionList = viewModel.consumptionList.value!!
                    val existingConsumption = consumptionList.find { it.product.id == consumption.product.id }
                    if (existingConsumption != null) {
                        existingConsumption.value += consumption.measure.toMeasure(
                            consumption.value,
                            existingConsumption.measure
                        )
                    } else consumptionList.add(consumption)
                    viewModel.consumptionList.value = consumptionList
                } catch (e: ConsumptionIngredientsException) {
                    Toast.makeText(context!!, e.message, Toast.LENGTH_LONG).show()
                } catch (e: MeasureConversionException) {
                    Toast.makeText(context!!, e.message, Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    companion object {
        fun newInstance() =
            ConsumptionIngredientsAddDialogFragment()
    }
}