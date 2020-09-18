package com.hotmail.leon.zimmermann.homeassistant.app.ui.overview.consumption

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.ConsumptionFragmentComponentBinding
import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.ConsumptionException
import kotlinx.android.synthetic.main.consumption_fragment_component.*

class ConsumptionFragmentComponent : Fragment() {
    private lateinit var viewModel: ConsumptionViewModel
    private lateinit var binding: ConsumptionFragmentComponentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.consumption_fragment_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatabinding(view)
        initLayout(view)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ConsumptionViewModel::class.java)
    }

    private fun initDatabinding(view: View) {
        binding = ConsumptionFragmentComponentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initLayout(view: View) {
        product_mode_button.setOnClickListener { viewModel.consumptionMode.value = viewModel.productMode }
        template_mode_button.setOnClickListener { viewModel.consumptionMode.value = viewModel.templateMode }
        meal_mode_button.setOnClickListener { viewModel.consumptionMode.value = viewModel.mealMode }
        viewModel.consumptionMode = MutableLiveData(viewModel.productMode)
        viewModel.consumptionMode.observe(viewLifecycleOwner, Observer { consumptionMode ->
            initNameInput(consumptionMode)
            initAdditionalFields(consumptionMode)
            initConsumeButton(view, consumptionMode)
        })
    }

    private fun initNameInput(consumptionMode: ConsumptionMode) {
        name_input.setAdapter(
            ArrayAdapter(
                context!!,
                android.R.layout.simple_list_item_1,
                consumptionMode.nameList
            )
        )
        name_input.setText("")
    }

    private fun initAdditionalFields(consumptionMode: ConsumptionMode) {
        additionalFieldsContainer.removeAllViews()
        consumptionMode.getAdditionalFieldsView(context!!)?.let {
            additionalFieldsContainer.addView(it)
        }
    }

    private fun initConsumeButton(view: View, consumptionMode: ConsumptionMode) {
        consume_button.setOnClickListener {
            try {
                consumptionMode.consume(view)
            } catch (e: ConsumptionException) {
                Snackbar.make(view, e.message!!, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}