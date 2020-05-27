package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview.consumption

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.ConsumptionFragmentComponentBinding
import kotlinx.android.synthetic.main.consumption_fragment_component.*

class ConsumptionFragmentComponent : Fragment() {
    private lateinit var viewModel: ConsumptionViewModel
    private lateinit var binding: ConsumptionFragmentComponentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ConsumptionViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.consumption_fragment_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatabinding(view)
        product_mode_button.setOnClickListener { viewModel.consumptionMode.value = viewModel.productMode }
        template_mode_button.setOnClickListener { viewModel.consumptionMode.value = viewModel.templateMode }
        meal_mode_button.setOnClickListener { viewModel.consumptionMode.value = viewModel.mealMode }
        viewModel.consumptionMode.observe(this, Observer { consumptionMode ->
            name_input.setAdapter(
                ArrayAdapter(
                    context!!,
                    android.R.layout.simple_list_item_1,
                    consumptionMode.nameList
                )
            )
            name_input.setText("")
            additionalFieldsContainer.removeAllViews()
            consumptionMode.getAdditionalFieldsView(context!!)?.let {
                additionalFieldsContainer.addView(it)
            }
            consume_button.setOnClickListener { consumptionMode.consume(view) }
        })
    }

    private fun initDatabinding(view: View) {
        binding = ConsumptionFragmentComponentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}