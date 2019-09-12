package com.hotmail.leon.zimmermann.homeassistant.stockManagement.itemDialog

import android.app.AlertDialog
import android.view.View
import com.hotmail.leon.zimmermann.homeassistant.product.Product
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.utils.MutableListLiveData
import kotlinx.android.synthetic.main.stock_item_dialog.view.*

sealed class StockItemDialogMode {
    abstract fun apply(builder: AlertDialog.Builder, view: View)
}

class StockItemDialogAddMode(private val productList: MutableListLiveData<Product>) : StockItemDialogMode() {
    override fun apply(builder: AlertDialog.Builder, view: View) {
        initializeView(view)
        addAddButton(builder, view)
    }

    private fun initializeView(view: View) {
        view.name_et.setText("")
        view.max_et.setText("0")
        view.min_et.setText("0")
        view.current_et.setText("0")
    }

    private fun addAddButton(builder: AlertDialog.Builder, view: View) {
        builder.setPositiveButton(R.string.submit) { dialogInterface, i ->
            addProduct(view)
        }
    }

    private fun addProduct(view: View) {
        productList.add(
            Product(
                view.name_et.text.toString(),
                if (view.max_et.text.isNotBlank()) view.max_et.text.toString().toInt() else 0,
                if (view.min_et.text.isNotBlank()) view.min_et.text.toString().toInt() else 0,
                if (view.current_et.text.isNotBlank()) view.current_et.text.toString().toInt() else 0
            )
        )
    }
}

class StockItemDialogEditMode(
    private val productList: MutableListLiveData<Product>,
    private val product: Product
) : StockItemDialogMode() {

    override fun apply(builder: AlertDialog.Builder, view: View) {
        initializeView(view)
        addDeleteButton(builder)
        addUpdateButton(builder, view)
    }

    private fun initializeView(view: View) {
        view.name_et.setText(product.name)
        view.max_et.setText(product.max.toString())
        view.min_et.setText(product.min.toString())
        view.current_et.setText(product.current.toString())
    }

    private fun addUpdateButton(builder: AlertDialog.Builder, view: View) {
        builder.setPositiveButton(R.string.submit) { dialogInterface, i ->
            updateProductAttributes(view)
        }
    }

    private fun updateProductAttributes(view: View) {
        productList.change(product) {
            it.name = view.name_et.text.toString()
            it.max = view.max_et.text.toString().toInt()
            it.min = view.min_et.text.toString().toInt()
            it.current = view.current_et.text.toString().toInt()
        }
    }

    private fun addDeleteButton(builder: AlertDialog.Builder) {
        builder.setNegativeButton(R.string.delete) { dialogInterface, i ->
            productList.remove(product)
        }
    }
}