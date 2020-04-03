package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.details

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.meal.MealWithIngredients
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import kotlinx.android.synthetic.main.dinner_details_fragment.*

class MealDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = MealDetailsFragment()
    }

    private lateinit var viewModel: MealDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dinner_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MealDetailsViewModel::class.java)
        arguments?.let { bundle ->
            val consumptionList = bundle.getSerializable("editConsumptionList") as MealWithIngredients
            name_input.text = consumptionList.meal.name
            duration_input.text = consumptionList.meal.duration?.toString() ?: ""
            consumption_details_description_tv.text = consumptionList.meal.description ?: ""
            consumption_details_ingredients_tv.text = consumptionList.ingredients
                .map {
                    Triple(
                        viewModel.productList.value?.first { product -> product.id == it.productId }?.name,
                        it.value,
                        Measure.values()[it.measureId.toInt()].abbreviation
                    )
                }
                .filter { it.first != null }
                .joinToString { "- ${it.first}: ${it.second}${it.third}" }
            consumption_details_instructions_tv.text = consumptionList.meal.instructions ?: ""
            // TODO Implement
        } ?: throw RuntimeException("No arguments passed!")
    }

}
