package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview.consumption

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.Measure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.MeasureRepository
import kotlinx.android.synthetic.main.product_additional_fields.view.*
import kotlinx.android.synthetic.main.shopping_edit_dialog.view.*
import org.jetbrains.anko.layoutInflater

sealed class ConsumptionMode {
    abstract val nameList: Array<String>
    abstract fun getAdditionalFieldsView(context: Context): View?
    abstract fun consume(view: View)
}

class ProductMode(private val viewModel: ConsumptionViewModel) : ConsumptionMode() {
    override val nameList: Array<String>
        get() = viewModel.productList.map { it.name }.toTypedArray()

    override fun getAdditionalFieldsView(context: Context): View {
        val view = context.layoutInflater.inflate(R.layout.product_additional_fields, null)
        view.measure_input.setAdapter(ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            viewModel.measures.map { it.name }))
        return view
    }

    override fun consume(view: View) {
        val productName = view.name_input.text.toString()
        val product = viewModel.productList.firstOrNull { it.name == productName }
        val quantity = view.quantity_input.text.toString().toFloat()
        val measure = MeasureRepository.getMeasureForName(view.measure_input.text.toString())
        TODO("Not Implemented")
    }
}

class TemplateMode(private val viewModel: ConsumptionViewModel) : ConsumptionMode() {
    override val nameList: Array<String>
        get() = emptyArray()

    override fun getAdditionalFieldsView(context: Context): View? = null

    override fun consume(view: View) {
        TODO("not implemented")
    }
}

class MealMode(private val viewModel: ConsumptionViewModel) : ConsumptionMode() {
    override val nameList: Array<String>
        get() = emptyArray()
        //get() = viewModel.mealList.map { it.name }.toTypedArray()

    override fun getAdditionalFieldsView(context: Context): View? = null

    override fun consume(view: View) {
        TODO("not implemented")
    }
}