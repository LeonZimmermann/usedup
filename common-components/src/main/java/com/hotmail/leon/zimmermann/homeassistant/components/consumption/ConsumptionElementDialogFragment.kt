package com.hotmail.leon.zimmermann.homeassistant.components.consumption

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hotmail.leon.zimmermann.homeassistant.components.R
import kotlinx.android.synthetic.main.consumption_element_dialog_fragment.view.*

class ConsumptionElementDialogFragment(callback: (consumptionElement: ConsumptionElement) -> Unit) : DialogFragment() {

  private lateinit var viewModel: ConsumptionElementDialogViewModel
  //private lateinit var binding: ConsumptionElementDialogFragmentBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initViewModel()
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return activity?.let {
      val view = requireActivity().layoutInflater.inflate(R.layout.consumption_element_dialog_fragment, null)
      initNameInput(view)
      initMeasureInput(view)
      MaterialAlertDialogBuilder(context)
        .setView(view)
        .setPositiveButton(R.string.submit) { _, _ -> viewModel.onPositiveButtonClicked() }
        .setNegativeButton(R.string.cancel) { dialogInterface, _ -> viewModel.onNegativeButtonClicked(dialogInterface) }
        .create()
    } ?: throw IllegalArgumentException("Activity cannot be null")
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    //initDatabinding(view)
  }

  private fun initViewModel() {
    viewModel = ViewModelProviders.of(this).get(ConsumptionElementDialogViewModel::class.java)
  }
/*
  private fun initDatabinding(view: View) {
    binding = ConsumptionElementDialogFragmentBinding.bind(view)
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
  }
*/
  private fun initNameInput(view: View) {
    viewModel.productNames.observe(viewLifecycleOwner, Observer { productNames ->
      view.name_input.setAdapter(ArrayAdapter(requireContext(), R.layout.list_item, productNames))
    })
  }

  private fun initMeasureInput(view: View) {
    viewModel.measureNames.observe(viewLifecycleOwner, Observer { measureNames ->
      view.measure_input.setAdapter(ArrayAdapter(requireContext(), R.layout.list_item, measureNames))
    })
  }
}