package com.hotmail.leon.zimmermann.homeassistant.components.consumption

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hotmail.leon.zimmermann.homeassistant.components.R
import com.hotmail.leon.zimmermann.homeassistant.components.databinding.ConsumptionElementDialogFragmentBinding
import kotlinx.android.synthetic.main.consumption_element_dialog_fragment.view.*

class ConsumptionElementDialogFragment(private val callback: (consumptionElement: ConsumptionElement) -> Unit) :
  DialogFragment() {

  private val viewModel: ConsumptionElementDialogViewModel by viewModels()
  private lateinit var binding: ConsumptionElementDialogFragmentBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initCallback()
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return activity?.let {
      val view = requireActivity().layoutInflater.inflate(R.layout.consumption_element_dialog_fragment, null)
      initDatabinding(view)
      initNameInput(view)
      initMeasureInput(view)
      MaterialAlertDialogBuilder(context)
        .setView(view)
        .setPositiveButton(R.string.submit) { _, _ -> viewModel.onPositiveButtonClicked() }
        .setNegativeButton(R.string.cancel) { dialogInterface, _ -> viewModel.onNegativeButtonClicked(dialogInterface) }
        .create()
    } ?: throw IllegalArgumentException("Activity cannot be null")
  }


  private fun initCallback() {
    viewModel.callback = callback
  }

  private fun initDatabinding(view: View) {
    binding = ConsumptionElementDialogFragmentBinding.bind(view).apply {
      lifecycleOwner = this@ConsumptionElementDialogFragment
      viewModel = this@ConsumptionElementDialogFragment.viewModel
    }
  }

  private fun initNameInput(view: View) {
    viewModel.productNames.observe(this, Observer { productNames ->
      view.name_input.setAdapter(ArrayAdapter(requireContext(), R.layout.list_item, productNames))
    })
  }

  private fun initMeasureInput(view: View) {
    viewModel.measureNames.observe(this, Observer { measureNames ->
      view.measure_input.setAdapter(ArrayAdapter(requireContext(), R.layout.list_item, measureNames))
    })
  }
}