package com.hotmail.leon.zimmermann.homeassistant.ui.consumption.details

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionList
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import kotlinx.android.synthetic.main.consumption_details_fragment.*

class ConsumptionDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ConsumptionDetailsFragment()
    }

    private lateinit var viewModel: ConsumptionDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.consumption_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ConsumptionDetailsViewModel::class.java)
        // TODO: Use the ViewModel
        arguments?.let { bundle ->
            val consumptionList = bundle.getSerializable("consumptionList") as ConsumptionList
            consumption_details_name_tv.text = consumptionList.metaData.name
            consumption_details_duration_tv.text = consumptionList.metaData.duration?.toString() ?: ""
            consumption_details_description_tv.text = consumptionList.metaData.description ?: ""
            consumption_details_ingredients_tv.text = consumptionList.consumptions
                .map {
                    Triple(
                        viewModel.productList.value?.first { product -> product.id == it.productId }?.name,
                        it.value,
                        Measure.values()[it.measureId].abbreviation
                    )
                }
                .filter { it.first != null }
                .joinToString { "- ${it.first}: ${it.second}${it.third}" }
            consumption_details_instructions_tv.text = consumptionList.metaData.instructions ?: ""
            // TODO Implement
        } ?: throw RuntimeException("No arguments passed!")
    }

}
