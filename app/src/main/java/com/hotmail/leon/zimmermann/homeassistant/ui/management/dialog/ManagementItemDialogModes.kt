package com.hotmail.leon.zimmermann.homeassistant.ui.management.dialog

import android.app.AlertDialog
import android.view.View
import com.hotmail.leon.zimmermann.homeassistant.models.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.management_item_dialog.view.*
import java.io.Serializable


sealed class ManagementItemDialogHandler : Serializable

object ManagementItemDialogAddHandler : ManagementItemDialogHandler() {

    fun initializeButtons(builder: AlertDialog.Builder, view: View, onAdd: (productEntity: ProductEntity) -> Unit) {
        builder.setPositiveButton(R.string.submit) { dialogInterface, i ->
            addProduct(
                view,
                onAdd
            )
        }
    }

    fun initializeView(view: View) {
        view.name_et.setText("")
        view.max_et.setText("0")
        view.min_et.setText("0")
        view.quantity_tv.setText("0")
    }

    private fun addProduct(view: View, onAdd: (productEntity: ProductEntity) -> Unit) {
        val name = view.name_et.text.toString()
        val quantity = if (view.quantity_tv.text.isNotBlank()) view.quantity_tv.text.toString().toInt() else 0
        val min =if (view.min_et.text.isNotBlank()) view.min_et.text.toString().toInt() else 0
        val max =if (view.max_et.text.isNotBlank()) view.max_et.text.toString().toInt() else 0
        val capacity = quantity.toDouble()
        // TODO Add Validation
        // Example if (max < min) ...
        onAdd(ProductEntity(name, quantity, min, max, capacity))
    }
}

object ManagementItemDialogEditHandler : ManagementItemDialogHandler() {

    fun initializeButtons(
        builder: AlertDialog.Builder, view: View,
        onEdit: (name: String, quantity: Int, min: Int, max: Int) -> Unit,
        onDelete: () -> Unit
    ) {
        addUpdateButton(
            builder,
            view,
            onEdit
        )
        addDeleteButton(
            builder,
            onDelete
        )
    }

    fun initializeView(view: View, productEntity: ProductEntity) {
        view.name_et.setText(productEntity.name)
        view.max_et.setText(productEntity.max.toString())
        view.min_et.setText(productEntity.min.toString())
        view.quantity_tv.setText(productEntity.quantity.toString())
    }

    private fun addUpdateButton(
        builder: AlertDialog.Builder,
        view: View,
        onEdit: (name: String, quantity: Int, min: Int, max: Int) -> Unit
    ) {
        builder.setPositiveButton(R.string.submit) { dialogInterface, i ->
            onEdit(
                view.name_et.text.toString(),
                if (view.quantity_tv.text.isNotBlank()) view.quantity_tv.text.toString().toInt() else 0,
                if (view.min_et.text.isNotBlank()) view.min_et.text.toString().toInt() else 0,
                if (view.max_et.text.isNotBlank()) view.max_et.text.toString().toInt() else 0
            )
        }
    }

    private fun addDeleteButton(builder: AlertDialog.Builder, onDelete: () -> Unit) {
        builder.setNegativeButton(R.string.delete) { dialogInterface, i -> onDelete() }
    }
}