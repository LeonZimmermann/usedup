package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.management.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import java.lang.RuntimeException

class ManagementItemDialogFragment() : DialogFragment() {

    private lateinit var viewModel: ManagementItemDialogViewModel
    private lateinit var handler: ManagementItemDialogHandler
    private var productId: Int? = null

    constructor(handler: ManagementItemDialogHandler, productId: Int?) : this() {
        this.handler = handler
        this.productId = productId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this)[ManagementItemDialogViewModel::class.java]
        if (savedInstanceState != null) {
            handler = savedInstanceState.getSerializable("handler") as? ManagementItemDialogHandler
                ?: throw RuntimeException("Handler not found!")
            productId = savedInstanceState.getSerializable("productId") as Int?
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = requireActivity().layoutInflater.inflate(R.layout.management_item_dialog, null)
            val builder = AlertDialog.Builder(it)
                .setTitle("ProductEntity")
                .setView(view)
                .setNeutralButton(R.string.cancel) { dialogInterface, i -> dialogInterface.cancel() }
            initializeButtons(builder, view)
            viewModel.productEntityList.observe(this, Observer { productList ->
                initializeView(view, productList.firstOrNull { it.id == productId })
            })
            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }

    private fun initializeButtons(builder: AlertDialog.Builder, view: View) {
        when (val handler = this.handler) {
            is ManagementItemDialogAddHandler -> ManagementItemDialogAddHandler
                .initializeButtons(builder, view, viewModel::insert)
            is ManagementItemDialogEditHandler -> ManagementItemDialogEditHandler
                .initializeButtons(builder, view, ::onEdit, ::onDelete)
        }
    }

    private fun onEdit(name: String, quantity: Double, measure: Measure, min: Int, max: Int, capacity: Double) {
        val productList = viewModel.productEntityList.value!!
        val product = productList.first { it.id == productId }
        product.name = name
        product.quantity = quantity
        product.min = min
        product.max = max
        product.capacity = capacity
        product.measureId = measure.id
        viewModel.update(product)
    }

    private fun onDelete() {
        val productList = viewModel.productEntityList.value!!
        val product = productList.first { it.id == productId }
        viewModel.delete(product)
    }

    private fun initializeView(view: View, productEntity: ProductEntity?) {
        when (this.handler) {
            is ManagementItemDialogAddHandler -> ManagementItemDialogAddHandler.initializeView(view)
            is ManagementItemDialogEditHandler -> ManagementItemDialogEditHandler.initializeView(view, productEntity!!)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("handler", handler)
        outState.putSerializable("productId", productId)
    }
}