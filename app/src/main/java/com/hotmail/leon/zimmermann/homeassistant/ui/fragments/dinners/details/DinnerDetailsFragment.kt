package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.dinners.details

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionList
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import kotlinx.android.synthetic.main.dinner_details_fragment.*

class DinnerDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DinnerDetailsFragment()
    }

    private lateinit var viewModel: DinnerDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dinner_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DinnerDetailsViewModel::class.java)
        arguments?.let { bundle ->
            val consumptionList = bundle.getSerializable("editConsumptionList") as ConsumptionList
            name_input.text = consumptionList.metaData.name
            duration_input.text = consumptionList.metaData.duration?.toString() ?: ""
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
