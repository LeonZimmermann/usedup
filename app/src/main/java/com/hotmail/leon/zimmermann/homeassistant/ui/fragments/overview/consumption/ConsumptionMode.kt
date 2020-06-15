package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview.consumption

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.ConsumptionException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import kotlinx.android.synthetic.main.consumption_fragment_component.*
import kotlinx.android.synthetic.main.product_additional_fields.view.*
import kotlinx.android.synthetic.main.shopping_edit_dialog.view.*
import kotlinx.android.synthetic.main.shopping_edit_dialog.view.name_input
import org.jetbrains.anko.layoutInflater

sealed class ConsumptionMode {
    abstract val nameList: Array<String>
    abstract fun getAdditionalFieldsView(context: Context): View?
    abstract fun consume(view: View)
}

class ProductMode(private val viewModel: ConsumptionViewModel) : ConsumptionMode() {
    override val nameList: Array<String>
        get() = viewModel.productList.map { it.second.name!! }.toTypedArray()

    override fun getAdditionalFieldsView(context: Context): View {
        val view = context.layoutInflater.inflate(R.layout.product_additional_fields, null)
        view.measure_input.setAdapter(
            ArrayAdapter(
                context,
                android.R.layout.simple_list_item_1,
                viewModel.measures.map { it.second.name })
        )
        return view
    }

    override fun consume(view: View) {
        val productName = view.name_input.text.toString()
        val product = ProductRepository.getProductForName(productName)
        val quantity = view.quantity_input.text.toString().toDouble()
        val measure = MeasureRepository.getMeasureForName(view.measure_input.text.toString())
        val existingQuantity = product.quantity!!
        val conversionQuantity = quantity.toBase(measure) / product.capacity!!
        if (conversionQuantity <= existingQuantity) {
            viewModel.database.collection(Product.COLLECTION_NAME)
                .document(ProductRepository.getId(productName))
                .update(mapOf("quantity" to product.quantity!! - conversionQuantity))
            view.name_input.setText("")
            view.quantity_input.setText("")
            view.measure_input.setText("")
            view.name_input.requestFocus()
        } else throw ConsumptionException("Not enough $productName")
    }
}

class TemplateMode(private val viewModel: ConsumptionViewModel) : ConsumptionMode() {
    override val nameList: Array<String>
        get() = viewModel.templateList.map { it.second.name!! }.toTypedArray()

    override fun getAdditionalFieldsView(context: Context): View? = null

    override fun consume(view: View) {
        val templateName = view.name_input.text.toString()
        val template = TemplateRepository.getTemplateForName(templateName)
        TODO("not implemented")
    }
}

class MealMode(private val viewModel: ConsumptionViewModel) : ConsumptionMode() {
    override val nameList: Array<String>
        get() = viewModel.mealList.map { it.second.name!! }.toTypedArray()

    override fun getAdditionalFieldsView(context: Context): View? = null

    override fun consume(view: View) {
        val mealName = view.name_input.text.toString()
        val meal = MealRepository.getMealForName(mealName)
        TODO("not implemented")
    }
}