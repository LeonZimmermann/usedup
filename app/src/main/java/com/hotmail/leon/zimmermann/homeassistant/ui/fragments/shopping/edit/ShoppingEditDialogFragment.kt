package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping.edit

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping.ShoppingEntry
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping.ShoppingViewModel
import kotlinx.android.synthetic.main.shopping_edit_dialog.view.*
import java.lang.NumberFormatException

class ShoppingEditDialogFragment : DialogFragment() {

    private lateinit var viewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ShoppingViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let {
            val builder = AlertDialog.Builder(context!!)
            val view = requireActivity().layoutInflater.inflate(R.layout.shopping_edit_dialog, null).apply {
                initView(this)
                builder.setView(this)
            }
            setTitle(builder)
            builder.setPositiveButton(R.string.submit) { dialogInterface, i ->
                try {
                    if (isEditMode()) {
                        viewModel.editShoppingEntry(
                            viewModel.editShoppingEntryIndex!!,
                            validateAndGetProductName(view),
                            validateAndGetAmount(view)
                        )
                    } else {
                        viewModel.addShoppingEntry(
                            ShoppingEntry(
                                validateAndGetProductName(view),
                                validateAndGetAmount(view)
                            )
                        )
                    }
                } catch (e: InvalidParametersException) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
                viewModel.editShoppingEntryIndex = null
            }
            builder.setNeutralButton(R.string.cancel) { dialogInterface, i ->
                viewModel.editShoppingEntryIndex = null
                dialogInterface.cancel()
            }
            if (isEditMode()) builder.setNegativeButton(R.string.delete) { dialogInterface, i ->
                viewModel.editShoppingEntryIndex?.let {
                    viewModel.removeShoppingEntryAt(it)
                }
                viewModel.editShoppingEntryIndex = null
            }
            return builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }

    private fun initView(view: View) {
        initProductList(view)
        if (isEditMode()) initWithEditData(view)
    }

    private fun initProductList(view: View) {
        viewModel.productList.observe(this, Observer { productList ->
            view.product_name_input.setAdapter(
                ArrayAdapter(context!!, android.R.layout.simple_list_item_1, productList)
            )
        })
    }

    private fun initWithEditData(view: View) {
        viewModel.shoppingList.value!!.let { shoppingList ->
            val shoppingEntry = shoppingList[viewModel.editShoppingEntryIndex!!]
            view.product_name_input.setText(shoppingEntry.product.name)
            view.amount_input.setText(shoppingEntry.amount.toString())
        }
    }

    private fun setTitle(builder: AlertDialog.Builder) {
        if (isEditMode()) builder.setTitle("Edit Product")
        else builder.setTitle("Add Product")
    }

    private fun validateAndGetProductName(view: View): ProductEntity {
        val productNameValue = view.product_name_input.text.toString()
        if (productNameValue.isEmpty()) throw InvaldProductNameException("Supply a Product Name")
        return viewModel.productList.value!!.find { it.name == productNameValue }
            ?: throw InvaldProductNameException("Product could not be found")
    }

    private fun validateAndGetAmount(view: View): Int {
        val amountValue = view.amount_input.text.toString()
        if (amountValue.isEmpty()) throw InvalidAmountException("Supply an amount")
        return try {
            amountValue.toInt()
        } catch (e: NumberFormatException) {
            throw InvalidAmountException("Amount needs to be an integer number")
        }
    }

    private fun isEditMode() = viewModel.editShoppingEntryIndex != null
}

sealed class InvalidParametersException(message: String) : Exception(message)
class InvaldProductNameException(message: String) : InvalidParametersException(message)
class InvalidAmountException(message: String) : InvalidParametersException(message)