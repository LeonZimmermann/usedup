package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.ui.components.SimpleProductPreviewAdapter
import kotlinx.android.synthetic.main.overview_fragment.*

class OverviewFragment : Fragment() {

    companion object {
        fun newInstance() = OverviewFragment()
    }

    private lateinit var viewModel: OverviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.overview_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overview_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)
        initializeTransactionCard()
        initializeDiscrepancyCard()
        initializeManagementCard()
        initializeCalendarCard()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.settings_option -> {
            findNavController().navigate(R.id.action_global_settings_fragment)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun initializeTransactionCard() {
        advanced_consumption_button.setOnClickListener {
            // TODO Implement
        }
        consumption_browser_button.setOnClickListener {
            findNavController().navigate(R.id.action_global_dinner_browser_fragment)
        }
        measure_input.adapter =
            ArrayAdapter(context!!, android.R.layout.simple_list_item_1, Measure.values())
        /*
        consumption_consume_button.setOnClickListener {
            try {
                val productName = product_name_input.text.toString()
                val product = viewModel.productEntityList.value!!.firstOrNull { it.name == productName }
                    ?: throw InvalidProductNameException()
                val value = quantity_change_input.text.toString().takeIf { it.isNotEmpty() }?.toDouble()
                    ?: throw InvalidQuantityChangeException()
                val measure = measure_input.selectedItem as Measure
                product.reduce(value, measure)
                viewModel.update(product)
            } catch (e: ConsumptionIngredientsException) {
                Toast.makeText(context!!, e.message, Toast.LENGTH_LONG).show()
            } catch (e: ProductEntity.ProductReductionException) {
                Toast.makeText(context!!, e.message, Toast.LENGTH_LONG).show()
            }
        }
         */
    }

    private fun initializeDiscrepancyCard() {
        val adapter = SimpleProductPreviewAdapter(context!!)
        overview_discrepancy_container.adapter = adapter
        overview_discrepancy_container.layoutManager = LinearLayoutManager(context!!)
        viewModel.productEntityList.observe(this, Observer {
            it?.let { adapter.productAmountList = it.map { product -> Pair(product, product.discrepancy) } }
        })
        shopping_button.setOnClickListener {
            findNavController().navigate(R.id.action_overview_fragment_to_shopping_fragment)
        }
    }

    private fun initializeManagementCard() {
        management_button.setOnClickListener {
            findNavController().navigate(R.id.action_overview_fragment_to_management_fragment)
        }
    }

    private fun initializeCalendarCard() {
        calendar_button.setOnClickListener {
            findNavController().navigate(R.id.action_overview_fragment_to_calendar_fragment)
        }
    }

}
