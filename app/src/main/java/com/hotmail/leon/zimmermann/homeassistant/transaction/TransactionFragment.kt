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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.product.Product
import kotlinx.android.synthetic.main.shopping_fragment.*
import kotlinx.android.synthetic.main.transaction_fragment.*

class TransactionFragment : Fragment() {

    private open inner class TransactionException(message: String) : Exception(message)
    private inner class InvalidQuantityChangeException : TransactionException("Invalid Quantity Change")
    private inner class InvalidProductNameException : TransactionException("Invalid Product Name")
    private inner class NoTransactionsException : TransactionException("No transactions to be made")

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
        initializeAddButton()
        initializeTransactionList()
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

    private fun initializeAddButton() {
        add_button.setOnClickListener {
            try {
                val (product, quantityChange) = getProductAndQuantityChange(viewModel.productList.value!!)
                val transactionList = viewModel.transactionList.value!!
                transactionList.add(Pair(product, quantityChange))
                viewModel.transactionList.value = transactionList
            } catch (e: TransactionException) {
                Toast.makeText(context!!, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getProductAndQuantityChange(productList: List<Product>): Pair<Product, Int> {
        val name = product_name_input.text.toString()
        productList.firstOrNull { it.name == name } ?: throw InvalidProductNameException()
        val product = productList.firstOrNull { it.name == name } ?: throw InvalidProductNameException()
        val quantityChange = quantity_change_input.text.toString().takeIf { it.isNotEmpty() }?.toInt()
            ?: throw InvalidQuantityChangeException()
        return Pair(product, quantityChange)
    }

    private fun initializeTransactionList() {
        val adapter = TransactionBatchListAdapter(context!!)
        val layoutManager = LinearLayoutManager(context!!)
        val divider = DividerItemDecoration(transaction_list.context!!, layoutManager.orientation)
        divider.setDrawable(context!!.getDrawable(R.drawable.divider)!!)
        transaction_list.addItemDecoration(divider)
        transaction_list.adapter = adapter
        transaction_list.layoutManager = layoutManager
        viewModel.transactionList.observe(this, Observer { transactionList ->
            adapter.setTransactionList(transactionList)
        })
    }

    private fun initializeTransactionButton() {
        transaction_button.setOnClickListener {
            try {
                val transactionList = viewModel.transactionList.value!!
                if (transactionList.isEmpty()) throw NoTransactionsException()
                for ((product, quantityChange) in transactionList)
                    product.quantity += quantityChange
                viewModel.updateAll(transactionList.map { it.first })
                viewModel.transactionList.value = mutableListOf()
            } catch (e: TransactionException) {
                Toast.makeText(context!!, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
