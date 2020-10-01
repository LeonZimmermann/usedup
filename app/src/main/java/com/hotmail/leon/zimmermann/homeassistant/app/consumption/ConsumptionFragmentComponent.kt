package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.ConsumptionFragmentComponentBinding
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
    initNameInput()
    initMeasureInput()
    initErrorSnackbar()
  }

  private fun initViewModel() {
    viewModel = ViewModelProviders.of(this).get(ConsumptionViewModel::class.java)
  }

  private fun initDatabinding(view: View) {
    binding = ConsumptionFragmentComponentBinding.bind(view)
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
  }

  private fun initNameInput() {
    viewModel.nameList.observe(viewLifecycleOwner, Observer { nameList ->
      name_input.setAdapter(ArrayAdapter(context!!, android.R.layout.simple_list_item_1, nameList))
    })
    name_input.onItemClickListener = viewModel
  }

  private fun initMeasureInput() {
    viewModel.measureList.observe(viewLifecycleOwner, Observer { measureList ->
      measure_input.setAdapter(ArrayAdapter(context!!, android.R.layout.simple_list_item_1, measureList))
    })
  }

  private fun initErrorSnackbar() {
    viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
      Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show()
    })
  }
}