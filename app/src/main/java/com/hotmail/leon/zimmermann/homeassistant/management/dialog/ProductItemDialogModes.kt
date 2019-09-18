package com.hotmail.leon.zimmermann.homeassistant.management.dialog

import android.app.AlertDialog
import android.view.View
import com.hotmail.leon.zimmermann.homeassistant.product.Product
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.product_item_dialog.view.*

sealed class ProductItemDialogMode {
    abstract fun apply(builder: AlertDialog.Builder, view: View)
}

class ProductItemDialogAddMode(private val onAdd: (product: Product) -> Unit) : ProductItemDialogMode() {
    override fun apply(builder: AlertDialog.Builder, view: View) {
        initializeView(view)
        addAddButton(builder, view)
    }

    private fun initializeView(view: View) {
        view.name_et.setText("")
        view.max_et.setText("0")
        view.min_et.setText("0")
        view.quantity_et.setText("0")
    }

    private fun addAddButton(builder: AlertDialog.Builder, view: View) {
        builder.setPositiveButton(R.string.submit) { dialogInterface, i ->
            addProduct(view)
        }
    }

    private fun addProduct(view: View) {
        onAdd(
            Product(
                view.name_et.text.toString(),
                if (view.max_et.text.isNotBlank()) view.max_et.text.toString().toInt() else 0,
                if (view.min_et.text.isNotBlank()) view.min_et.text.toString().toInt() else 0,
                if (view.quantity_et.text.isNotBlank()) view.quantity_et.text.toString().toInt() else 0
            )
        )
    }
}

class ProductItemDialogEditMode(
    private val product: Product,
    val onEdit: (product: Product) -> Unit,
    val onDelete: (product: Product) -> Unit
) : ProductItemDialogMode() {

    override fun apply(builder: AlertDialog.Builder, view: View) {
        initializeView(view)
        addDeleteButton(builder)
        addUpdateButton(builder, view)
    }

    private fun initializeView(view: View) {
        view.name_et.setText(product.name)
        view.max_et.setText(product.max.toString())
        view.min_et.setText(product.min.toString())
        view.quantity_et.setText(product.quantity.toString())
    }

    private fun addUpdateButton(builder: AlertDialog.Builder, view: View) {
        builder.setPositiveButton(R.string.submit) { dialogInterface, i ->
            updateProductAttributes(view)
        }
    }

    private fun updateProductAttributes(view: View) {
        product.name = view.name_et.text.toString()
        product.quantity = view.quantity_et.text.toString().toInt()
        product.max = view.max_et.text.toString().toInt()
        product.min = view.min_et.text.toString().toInt()
        onEdit(product)
    }

    private fun addDeleteButton(builder: AlertDialog.Builder) {
        builder.setNegativeButton(R.string.delete) { dialogInterface, i ->
            onDelete(product)
        }
    }
}