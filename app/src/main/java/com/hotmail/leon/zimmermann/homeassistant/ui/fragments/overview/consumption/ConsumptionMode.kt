package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview.consumption

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.consumption.BatchConsumptionBuilder
import com.hotmail.leon.zimmermann.homeassistant.consumption.BatchConsumptionProcessor
import com.hotmail.leon.zimmermann.homeassistant.consumption.SingleConsumptionBuilder
import com.hotmail.leon.zimmermann.homeassistant.consumption.SingleConsumptionProcessor
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.ValueWithMeasure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.TemplateRepository
import kotlinx.android.synthetic.main.consumption_fragment_component.view.*
import kotlinx.android.synthetic.main.product_additional_fields.view.*
import org.jetbrains.anko.layoutInflater

sealed class ConsumptionMode {
    abstract val nameList: Array<String>
    abstract fun getAdditionalFieldsView(context: Context): View?
    abstract fun consume(view: View)
    abstract fun resetInputs(view: View)
}

class ProductMode(private val viewModel: ConsumptionViewModel) : ConsumptionMode() {
    override val nameList: Array<String>
        get() = viewModel.productList.map { it.name }.toTypedArray()

    override fun getAdditionalFieldsView(context: Context): View {
        val view = context.layoutInflater.inflate(R.layout.product_additional_fields, null)
        view.measure_input.setAdapter(
            ArrayAdapter(
                context,
                android.R.layout.simple_list_item_1,
                viewModel.measures.map { it.name })
        )
        return view
    }

    override fun consume(view: View) {
        val product = ProductRepository.getProductForName(view.name_input.text.toString())
        val measure = MeasureRepository.getMeasureForName(view.measure_input.text.toString())
        val quantity = view.quantity_input.text.toString().toDouble()
        SingleConsumptionBuilder()
            .addResultHandler(SingleConsumptionProcessor(viewModel.database, viewModel.viewModelScope))
            .addResultHandler(object : SingleConsumptionBuilder.ResultHandler {
                override fun onSuccess(newQuantity: Pair<Product, Double>) {
                    resetInputs(view)
                }
            }).consume(product, ValueWithMeasure(quantity, measure))
    }

    override fun resetInputs(view: View) {
        view.name_input.setText("")
        view.quantity_input.setText("")
        view.measure_input.setText("")
        view.name_input.requestFocus()
    }
}

class TemplateMode(private val viewModel: ConsumptionViewModel) : ConsumptionMode() {
    override fun getAdditionalFieldsView(context: Context): View? = null

    override val nameList: Array<String>
        get() = viewModel.templateList.map { it.name }.toTypedArray()

    override fun consume(view: View) {
        val templateName = view.name_input.text.toString()
        val template = TemplateRepository.getTemplateForName(templateName)
        val consumptions = template.components.map {
            Pair(
                ProductRepository.getProductForId(it.productId),
                ValueWithMeasure(it.value, MeasureRepository.getMeasureForId(it.measureId))
            )
        }
        BatchConsumptionBuilder()
            .addResultHandler(BatchConsumptionProcessor(viewModel.database, viewModel.viewModelScope))
            .addResultHandler(object : BatchConsumptionBuilder.ResultHandler {
                override fun onSuccess(batch: List<Pair<Product, Double>>) {
                    resetInputs(view)
                }
            })
            .consume(consumptions)
    }

    override fun resetInputs(view: View) {
        view.name_input.setText("")
        view.name_input.requestFocus()
    }
}

class MealMode(private val viewModel: ConsumptionViewModel) : ConsumptionMode() {
    override val nameList: Array<String>
        get() = viewModel.mealList.map { it.name }.toTypedArray()

    override fun getAdditionalFieldsView(context: Context): View? = null

    override fun consume(view: View) {
        val mealName = view.name_input.text.toString()
        val meal = MealRepository.getMealForName(mealName)
        val consumptions = meal.ingredients.map {
            Pair(
                ProductRepository.getProductForId(it.productId),
                ValueWithMeasure(it.value, MeasureRepository.getMeasureForId(it.measureId))
            )
        }
        BatchConsumptionBuilder()
            .addResultHandler(BatchConsumptionProcessor(viewModel.database, viewModel.viewModelScope))
            .addResultHandler(object : BatchConsumptionBuilder.ResultHandler {
                override fun onSuccess(batch: List<Pair<Product, Double>>) {
                    resetInputs(view)
                }
            })
            .consume(consumptions)
    }

    override fun resetInputs(view: View) {
        view.name_input.setText("")
        view.name_input.requestFocus()
    }
}