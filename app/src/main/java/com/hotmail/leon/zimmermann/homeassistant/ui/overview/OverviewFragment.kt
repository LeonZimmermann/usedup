package com.hotmail.leon.zimmermann.homeassistant.ui.overview

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.ui.consumption.ConsumptionException
import com.hotmail.leon.zimmermann.homeassistant.ui.consumption.InvalidProductNameException
import com.hotmail.leon.zimmermann.homeassistant.ui.consumption.InvalidQuantityChangeException
import kotlinx.android.synthetic.main.overview_fragment.*

class OverviewFragment : Fragment() {

    companion object {
        fun newInstance() = OverviewFragment()
    }

    private lateinit var viewModel: OverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.overview_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)
        initializeTransactionCard()
        initializeDiscrepancyCard()
        initializeManagementCard()
    }

    private fun initializeTransactionCard() {
        advanced_consumption_button.setOnClickListener {
            findNavController().navigate(R.id.action_global_consumption_fragment)
        }
        consumption_browser_button.setOnClickListener {
            findNavController().navigate(R.id.action_global_consumption_browser_fragment)
        }
        consumption_measure_input.adapter =
            ArrayAdapter(context!!, android.R.layout.simple_list_item_1, Measure.values())
        consumption_consume_button.setOnClickListener {
            try {
                val productName = consumption_product_name_input.text.toString()
                val product = viewModel.productEntityList.value!!.firstOrNull { it.name == productName }
                    ?: throw InvalidProductNameException()
                val value = consumption_quantity_change_input.text.toString().takeIf { it.isNotEmpty() }?.toDouble()
                    ?: throw InvalidQuantityChangeException()
                val measure = consumption_measure_input.selectedItem as Measure
                product.reduce(value, measure)
                viewModel.update(product)
            } catch (e: ConsumptionException) {
                Toast.makeText(context!!, e.message, Toast.LENGTH_LONG).show()
            } catch (e: ProductEntity.ProductReductionException) {
                Toast.makeText(context!!, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initializeDiscrepancyCard() {
        val adapter = DiscrepancyListAdapter(context!!)
        overview_discrepancy_container.adapter = adapter
        overview_discrepancy_container.layoutManager = LinearLayoutManager(context!!)
        viewModel.productEntityList.observe(this, Observer {
            it?.let { adapter.setProductList(it) }
        })
        shopping_button.setOnClickListener {
            findNavController().navigate(R.id.action_global_shopping_fragment)
        }
    }

    private fun initializeManagementCard() {
        management_button.setOnClickListener {
            findNavController().navigate(R.id.action_global_management_fragment)
        }
    }

}
