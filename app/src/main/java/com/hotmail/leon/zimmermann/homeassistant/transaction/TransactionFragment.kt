package com.hotmail.leon.zimmermann.homeassistant.transaction

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.Observer

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.product.Product
import kotlinx.android.synthetic.main.transaction_fragment.*

class TransactionFragment : Fragment() {

    private open inner class TransactionException(message: String) : Exception(message)
    private inner class InvalidQuantityChangeException : TransactionException("Invalid Quantity Change")
    private inner class InvalidProductNameException : TransactionException("Invalid Product Name")

    companion object {
        fun newInstance() = TransactionFragment()
    }

    private lateinit var viewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.transaction_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TransactionViewModel::class.java)
        initializeProductNameInput()
        initializeTransactionButton()
    }

    private fun initializeProductNameInput() {
        viewModel.productList.observe(this, Observer { productList ->
            product_name_input.setAdapter(ArrayAdapter<String>(
                context!!,
                android.R.layout.simple_dropdown_item_1line,
                productList.map { it.name }
            ))
        })
    }

    private fun initializeTransactionButton() {
        transaction_button.setOnClickListener {
            try {
                val productList = viewModel.productList.value!!
                val name = product_name_input.text.toString()
                val product = productList.firstOrNull { it.name == name } ?: throw InvalidProductNameException()
                val quantityChange = quantity_change_input.text.toString().takeIf { it.isNotEmpty() }?.toInt()
                    ?: throw InvalidQuantityChangeException()
                val quantityBefore = product.quantity
                product.quantity += quantityChange
                viewModel.update(product)
                Toast.makeText(
                    context!!,
                    "Successfully changed quantity of $name from $quantityBefore to ${product.quantity}",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: TransactionException) {
                Toast.makeText(context!!, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
