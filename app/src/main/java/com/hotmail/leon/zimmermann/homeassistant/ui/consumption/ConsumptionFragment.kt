package com.hotmail.leon.zimmermann.homeassistant.ui.consumption

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import kotlinx.android.synthetic.main.consumption_fragment.*

class ConsumptionFragment : Fragment() {

    private open inner class ConsumptionException(message: String) : Exception(message)
    private inner class InvalidQuantityChangeException : ConsumptionException("Invalid Quantity Change")
    private inner class InvalidProductNameException : ConsumptionException("Invalid ProductEntity Name")
    private inner class NoConsumptionsException : ConsumptionException("No consumptions specified")

    companion object {
        fun newInstance() = ConsumptionFragment()
    }

    private lateinit var viewModel: ConsumptionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.consumption_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ConsumptionViewModel::class.java)
        initializeProductNameInput()
        initializeAddButton()
        initializeConsumptionList()
        initializeConsumptionButton()
    }

    private fun initializeProductNameInput() {
        viewModel.productEntityList.observe(this, Observer { productList ->
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
                val (product, quantityChange) = getProductAndQuantityChange(viewModel.productEntityList.value!!)
                val consumptionList = viewModel.consumptionList.value!!
                consumptionList.add(Pair(product, quantityChange))
                viewModel.consumptionList.value = consumptionList
            } catch (e: ConsumptionException) {
                Toast.makeText(context!!, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getProductAndQuantityChange(productEntityList: List<ProductEntity>): Pair<ProductEntity, Int> {
        val name = product_name_input.text.toString()
        productEntityList.firstOrNull { it.name == name } ?: throw InvalidProductNameException()
        val product = productEntityList.firstOrNull { it.name == name } ?: throw InvalidProductNameException()
        val quantityChange = quantity_change_input.text.toString().takeIf { it.isNotEmpty() }?.toInt()
            ?: throw InvalidQuantityChangeException()
        return Pair(product, quantityChange)
    }

    private fun initializeConsumptionList() {
        val adapter =
            ConsumptionBatchListAdapter(context!!)
        val layoutManager = LinearLayoutManager(context!!)
        val divider = DividerItemDecoration(consumption_list.context!!, layoutManager.orientation)
        divider.setDrawable(context!!.getDrawable(R.drawable.divider)!!)
        consumption_list.addItemDecoration(divider)
        consumption_list.adapter = adapter
        consumption_list.layoutManager = layoutManager
        viewModel.consumptionList.observe(this, Observer { consumptionList ->
            adapter.setConsumptionList(consumptionList)
        })
    }

    private fun initializeConsumptionButton() {
        consumption_button.setOnClickListener {
            try {
                val consumptionList = viewModel.consumptionList.value!!
                if (consumptionList.isEmpty()) throw NoConsumptionsException()
                for ((product, quantityChange) in consumptionList)
                    product.quantity -= quantityChange
                viewModel.updateAll(consumptionList.map { it.first })
                viewModel.consumptionList.value = mutableListOf()
            } catch (e: ConsumptionException) {
                Toast.makeText(context!!, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
