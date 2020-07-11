package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview.consumption

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Value
import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.NotEnoughException
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
        val productName = view.name_input.text.toString()
        val product = ProductRepository.getProductForName(productName)
        val measure = MeasureRepository.getMeasureForName(view.measure_input.text.toString())
        val quantity = view.quantity_input.text.toString().toDouble()
        makeConsumption(product, measure, quantity, { newQuantity ->
            viewModel.database.collection(Product.COLLECTION_NAME)
                .document(product.id)
                .update(mapOf("quantity" to newQuantity))
            resetInputs(view)
        }, { missingQuantity ->
            throw NotEnoughException(listOf(product to Value(
                missingQuantity,
                measure
            )
            ))
        })
    }

    private fun resetInputs(view: View) {
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
        val batch = viewModel.database.batch()
        val missingQuantities = mutableListOf<Pair<Product, Value>>()
        template.components.forEach {
            val product = ProductRepository.getProductForId(it.product.id)
            val measure = MeasureRepository.getMeasureForId(it.measure.id)
            val quantity = it.value
            makeConsumption(product, measure, quantity, { newQuantity ->
                val productDocumentRef = viewModel.database.collection(Product.COLLECTION_NAME).document(product.id)
                batch.update(productDocumentRef, mapOf("quantity" to newQuantity))
            }, { missingQuantity -> missingQuantities.add(product to Value(
                missingQuantity,
                measure
            )
            ) })
        }
        if (missingQuantities.isEmpty()) batch.commit()
        else throw NotEnoughException(missingQuantities)
    }
}

class MealMode(private val viewModel: ConsumptionViewModel) : ConsumptionMode() {
    override val nameList: Array<String>
        get() = viewModel.mealList.map { it.name }.toTypedArray()

    override fun getAdditionalFieldsView(context: Context): View? = null

    override fun consume(view: View) {
        val mealName = view.name_input.text.toString()
        val meal = MealRepository.getMealForName(mealName)
        val batch = viewModel.database.batch()
        val missingQuantities = mutableListOf<Pair<Product, Value>>()
        meal.ingredients.forEach {
            val product = ProductRepository.getProductForId(it.product!!.id)
            val measure = MeasureRepository.getMeasureForId(it.measure!!.id)
            val quantity = it.value!!
            makeConsumption(product, measure, quantity, { newQuantity ->
                val productDocumentRef = viewModel.database.collection(Product.COLLECTION_NAME)
                    .document(product.id)
                batch.update(productDocumentRef, mapOf("quantity" to newQuantity))
            }, { missingQuantity -> missingQuantities.add(product to Value(
                missingQuantity,
                measure
            )
            ) })
        }
        if (missingQuantities.isEmpty()) batch.commit()
        else throw NotEnoughException(missingQuantities)
    }
}

private fun makeConsumption(
    product: Product,
    measure: Measure,
    quantity: Double,
    onSuccess: (newQuantity: Double) -> Unit,
    onFailure: (missingQuantity: Double) -> Unit
) {
    val existingQuantity = product.quantity * product.capacity
    val conversionQuantity = quantity.toBase(measure)
    val quantityDiff = existingQuantity - conversionQuantity
    if (quantityDiff < 0) onFailure(quantityDiff.toMeasure(measure))
    else onSuccess(product.quantity - quantity.toBase(measure) / product.capacity)
}