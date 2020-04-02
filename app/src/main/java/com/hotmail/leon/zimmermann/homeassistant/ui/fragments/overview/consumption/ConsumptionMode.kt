package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview.consumption

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import kotlinx.android.synthetic.main.product_additional_fields.view.*
import kotlinx.android.synthetic.main.shopping_edit_dialog.view.*
import org.jetbrains.anko.layoutInflater

sealed class ConsumptionMode {
    abstract val nameList: LiveData<Array<String>>
    abstract fun getAdditionalFieldsView(context: Context): View?
    abstract fun consume(view: View)
}

class ProductMode(private val viewModel: ConsumptionViewModel) : ConsumptionMode() {
    override val nameList: LiveData<Array<String>>
        get() = Transformations.map(viewModel.productList) { productList -> productList.map { it.name }.toTypedArray() }

    override fun getAdditionalFieldsView(context: Context): View {
        val view = context.layoutInflater.inflate(R.layout.product_additional_fields, null)
        view.measure_input.adapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            Measure.values().map { it.name })
        return view
    }

    override fun consume(view: View) {
        val productName = view.name_input.text.toString()
        val product = viewModel.productList.value!!.first { it.name == productName }
        val quantity = view.quantity_input.text.toString().toFloat()
        val measure = view.measure_input.selectedItem as Measure
        Toast.makeText(view.context!!, "product:$product, quantity:$quantity, measure:$measure", Toast.LENGTH_LONG)
            .show()
    }
}

class TemplateMode(private val viewModel: ConsumptionViewModel) : ConsumptionMode() {
    override val nameList: LiveData<Array<String>>
        get() = Transformations.map(viewModel.templateList) { templateList -> templateList.map { it.metaData.name }.toTypedArray() }

    override fun getAdditionalFieldsView(context: Context): View? = null

    override fun consume(view: View) {
        TODO("not implemented")
    }
}

class MealMode(private val viewModel: ConsumptionViewModel) : ConsumptionMode() {
    override val nameList: LiveData<Array<String>>
        get() = Transformations.map(viewModel.mealList) { mealList -> mealList.map { it.metaData.name }.toTypedArray() }

    override fun getAdditionalFieldsView(context: Context): View? = null

    override fun consume(view: View) {
        TODO("not implemented")
    }
}