package com.hotmail.leon.zimmermann.homeassistant.ui.consumption.ingredients.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.ui.consumption.ingredients.ConsumptionIngredientsException
import com.hotmail.leon.zimmermann.homeassistant.ui.consumption.ingredients.InvalidProductNameException
import com.hotmail.leon.zimmermann.homeassistant.ui.consumption.ingredients.InvalidQuantityChangeException
import com.hotmail.leon.zimmermann.homeassistant.ui.consumption.ingredients.fragment.*
import kotlinx.android.synthetic.main.consumption_ingredients_dialog.*

abstract class ConsumptionIngredientsDialogFragment protected constructor(private val title: String) : DialogFragment() {
    protected lateinit var viewModel: ConsumptionIngredientsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ConsumptionIngredientsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = requireActivity().layoutInflater.inflate(R.layout.consumption_ingredients_dialog, null)
            initializeView(view)
            val builder = AlertDialog.Builder(it)
                .setTitle(title)
                .setView(view)
            initializeButtons(builder, view)
            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }

    protected abstract fun initializeView(view: View)
    protected abstract fun initializeButtons(builder: AlertDialog.Builder, view: View)

    @Throws(ConsumptionIngredientsException::class)
    protected fun getProductAndQuantityChange(productEntityList: List<ProductEntity>): ConsumptionIngredientsTemplate {
        val name = dialog!!.product_name_input.text.toString()
        productEntityList.firstOrNull { it.name == name } ?: throw InvalidProductNameException()
        val product = productEntityList.firstOrNull { it.name == name } ?: throw InvalidProductNameException()
        val quantityChange = dialog!!.quantity_change_input.text.toString().takeIf { it.isNotEmpty() }?.toDouble()
            ?: throw InvalidQuantityChangeException()
        val measure = dialog!!.measure_input.selectedItem as Measure
        return ConsumptionIngredientsTemplate(
            product,
            quantityChange,
            measure
        )
    }
}