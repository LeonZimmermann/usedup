package com.hotmail.leon.zimmermann.homeassistant.ui.consumption

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.ui.consumption.save.SaveDialogFragment
import kotlinx.android.synthetic.main.consumption_fragment.*

class ConsumptionFragment : Fragment() {
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
        initializeMeasureInput()
        initializeAddButton()
        initializeConsumptionList()
        initializeConsumptionButton()
        initializeSaveButton()
    }

    private fun initializeProductNameInput() {
        viewModel.productEntityList.observe(this, Observer { productList ->
            consumption_product_name_input.setAdapter(ArrayAdapter<String>(
                context!!,
                android.R.layout.simple_dropdown_item_1line,
                productList.map { it.name }
            ))
        })
    }

    private fun initializeMeasureInput() {
        consumption_measure_input.adapter =
            ArrayAdapter(context!!, android.R.layout.simple_list_item_1, Measure.values())
    }

    private fun initializeAddButton() {
        consumption_add_button.setOnClickListener {
            try {
                val consumption = getProductAndQuantityChange(viewModel.productEntityList.value!!)
                val consumptionList = viewModel.consumptionList.value!!
                consumptionList.add(consumption)
                viewModel.consumptionList.value = consumptionList
            } catch (e: ConsumptionException) {
                Toast.makeText(context!!, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getProductAndQuantityChange(productEntityList: List<ProductEntity>): ConsumptionTemplate {
        val name = consumption_product_name_input.text.toString()
        productEntityList.firstOrNull { it.name == name } ?: throw InvalidProductNameException()
        val product = productEntityList.firstOrNull { it.name == name } ?: throw InvalidProductNameException()
        val quantityChange = consumption_quantity_change_input.text.toString().takeIf { it.isNotEmpty() }?.toDouble()
            ?: throw InvalidQuantityChangeException()
        val measure = consumption_measure_input.selectedItem as Measure
        return ConsumptionTemplate(
            product,
            quantityChange,
            measure
        )
    }

    private fun initializeConsumptionList() {
        val adapter = ConsumptionBatchListAdapter(context!!)
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
        consumption_consume_button.setOnClickListener {
            val consumptionsMade = mutableListOf<ConsumptionTemplate>()
            try {
                val consumptionList = viewModel.consumptionList.value!!
                if (consumptionList.isEmpty()) throw NoConsumptionsException()
                for (consumption in consumptionList) {
                    consumption.product.reduce(consumption.value, consumption.measure)
                    consumptionsMade.add(consumption)
                }
                viewModel.updateAll(consumptionList.map { it.product })
                viewModel.consumptionList.value = mutableListOf()
            } catch (e: ConsumptionException) {
                catchConsumptionException(consumptionsMade, e)
            } catch (e: ProductEntity.ProductReductionException) {
                catchConsumptionException(consumptionsMade, e)
            }
        }
    }

    private fun catchConsumptionException(consumptionsMade: List<ConsumptionTemplate>, exception: Exception) {
        consumptionsMade.forEach { it.product.reduce(-it.value, it.measure) }
        viewModel.updateAll(consumptionsMade.map { it.product })
        Toast.makeText(context!!, exception.message, Toast.LENGTH_LONG).show()
    }

    private fun initializeSaveButton() {
        consumption_save_button.setOnClickListener {
            SaveDialogFragment(object : SaveDialogFragment.OnSaveHandler {
                override fun onSave(name: String) {
                    viewModel.save(name)
                }
            }).show(fragmentManager!!, "SaveDialog")
        }
    }
}
