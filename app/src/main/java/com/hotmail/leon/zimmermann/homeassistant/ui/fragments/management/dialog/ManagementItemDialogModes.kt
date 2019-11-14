package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.management.dialog

import android.app.AlertDialog
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.Category
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import kotlinx.android.synthetic.main.management_item_dialog.view.*
import java.io.Serializable


sealed class ManagementItemDialogHandler : Serializable

object ManagementItemDialogAddHandler : ManagementItemDialogHandler() {

    fun initializeButtons(
        builder: AlertDialog.Builder,
        view: View,
        onAdd: (productEntity: ProductEntity) -> Unit
    ) {
        builder.setPositiveButton(R.string.submit) { dialogInterface, i ->
            try {
                addProduct(view, onAdd)
            } catch (e: Exception) {
                Toast.makeText(view.context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun initializeView(view: View) {
        view.management_item_dialog_measure_input.adapter = ArrayAdapter(
            view.management_item_dialog_measure_input.context, android.R.layout.simple_list_item_1, Measure.values()
        )
        view.management_item_dialog_category_input.adapter = ArrayAdapter(
            view.management_item_dialog_category_input.context, android.R.layout.simple_list_item_1, Category.values()
        )
    }

    private fun addProduct(
        view: View,
        onAdd: (productEntity: ProductEntity) -> Unit
    ) {
        val name = view.management_item_dialog_name_input.text.toString()
        val quantity =
            if (view.management_item_dialog_current_input.text.isNotBlank()) view.management_item_dialog_current_input.text.toString().toDouble() else 0.0
        val min =
            if (view.management_item_dialog_min_input.text.isNotBlank()) view.management_item_dialog_min_input.text.toString().toInt() else 0
        val max =
            if (view.management_item_dialog_max_input.text.isNotBlank()) view.management_item_dialog_max_input.text.toString().toInt() else 0
        val capacity =
            if (view.management_item_dialog_capacity_input.text.isNotBlank()) view.management_item_dialog_capacity_input.text.toString().toDouble() else 0.0
        val measure = view.management_item_dialog_measure_input.selectedItem as Measure
        val category = view.management_item_dialog_category_input.selectedItem as Category

        // TODO Add Validation
        // Example if (max < min) ...

        onAdd(ProductEntity(name, quantity, min, max, measure.toBaseMeasure(capacity), measure.id, category.id))
    }
}

object ManagementItemDialogEditHandler : ManagementItemDialogHandler() {

    fun initializeButtons(
        builder: AlertDialog.Builder, view: View,
        onEdit: (name: String, category: Category, quantity: Double, measure: Measure, min: Int, max: Int, capacity: Double) -> Unit,
        onDelete: () -> Unit
    ) {
        addUpdateButton(builder, view, onEdit)
        addDeleteButton(builder, onDelete)
    }

    fun initializeView(view: View, productEntity: ProductEntity) {
        view.management_item_dialog_name_input.setText(productEntity.name)
        view.management_item_dialog_capacity_input.setText(
            Measure.values()[productEntity.measureId].fromBaseMeasure(
                productEntity.capacity
            ).toString()
        )
        view.management_item_dialog_measure_input.adapter = ArrayAdapter(
            view.management_item_dialog_measure_input.context,
            android.R.layout.simple_list_item_1,
            Measure.values()
        )
        view.management_item_dialog_measure_input.setSelection(productEntity.measureId)
        view.management_item_dialog_category_input.adapter = ArrayAdapter(
            view.management_item_dialog_category_input.context, android.R.layout.simple_list_item_1, Category.values()
        )
        view.management_item_dialog_category_input.setSelection(productEntity.categoryId)
        view.management_item_dialog_current_input.setText(productEntity.quantity.toString())
        view.management_item_dialog_min_input.setText(productEntity.min.toString())
        view.management_item_dialog_max_input.setText(productEntity.max.toString())
    }

    private fun addUpdateButton(
        builder: AlertDialog.Builder,
        view: View,
        onEdit: (name: String, category: Category, quantity: Double, measure: Measure, min: Int, max: Int, capacity: Double) -> Unit
    ) {
        builder.setPositiveButton(R.string.submit) { dialogInterface, i ->
            val name = view.management_item_dialog_name_input.text.toString()
            val currentQuantity =
                if (view.management_item_dialog_current_input.text.isNotBlank()) view.management_item_dialog_current_input.text.toString().toDouble() else 0.0
            val measure = view.management_item_dialog_measure_input.selectedItem as Measure
            val category = view.management_item_dialog_category_input.selectedItem as Category
            val min =
                if (view.management_item_dialog_min_input.text.isNotBlank()) view.management_item_dialog_min_input.text.toString().toInt() else 0
            val max =
                if (view.management_item_dialog_max_input.text.isNotBlank()) view.management_item_dialog_max_input.text.toString().toInt() else 0
            val capacity =
                if (view.management_item_dialog_capacity_input.text.isNotBlank()) view.management_item_dialog_capacity_input.text.toString().toDouble() else 0.0
            onEdit(name, category, currentQuantity, measure, min, max, measure.toBaseMeasure(capacity))
        }
    }

    private fun addDeleteButton(builder: AlertDialog.Builder, onDelete: () -> Unit) {
        builder.setNegativeButton(R.string.delete) { dialogInterface, i -> onDelete() }
    }
}