package de.usedup.android.components.consumption

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.usedup.android.R
import dagger.hilt.android.AndroidEntryPoint
import de.usedup.android.databinding.ConsumptionElementDialogFragmentBinding
import kotlinx.android.synthetic.main.consumption_element_dialog_fragment.view.*
import java.io.Serializable

@AndroidEntryPoint
class ConsumptionElementDialogFragment @JvmOverloads constructor(private var callback: Callback? = null) :
  DialogFragment() {

  private val viewModel: ConsumptionElementDialogViewModel by viewModels()
  private lateinit var binding: ConsumptionElementDialogFragmentBinding

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    savedInstanceState?.let { callback = it.getSerializable(CALLBACK) as Callback }
    return activity?.let {
      val view = requireActivity().layoutInflater.inflate(R.layout.consumption_element_dialog_fragment, null)
      initDatabinding(view)
      initNameInput(view)
      initMeasureInput(view)
      MaterialAlertDialogBuilder(requireContext())
        .setView(view)
        .setPositiveButton(R.string.submit) { _, _ -> viewModel.onPositiveButtonClicked(requireNotNull(callback)) }
        .setNegativeButton(R.string.cancel) { dialogInterface, _ -> viewModel.onNegativeButtonClicked(dialogInterface) }
        .create()
    } ?: throw IllegalArgumentException("Activity cannot be null")
  }

  private fun initDatabinding(view: View) {
    binding = ConsumptionElementDialogFragmentBinding.bind(view).apply {
      lifecycleOwner = this@ConsumptionElementDialogFragment
      viewModel = this@ConsumptionElementDialogFragment.viewModel
    }
  }

  private fun initNameInput(view: View) {
    view.name_input.onItemClickListener = viewModel
    viewModel.productNames.observe(this, { productNames ->
      view.name_input.setAdapter(ArrayAdapter(requireContext(), R.layout.list_item, productNames))
    })
  }

  private fun initMeasureInput(view: View) {
    viewModel.measureNames.observe(this, { measureNames ->
      view.measure_input.setAdapter(ArrayAdapter(requireContext(), R.layout.list_item, measureNames))
    })
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putSerializable(CALLBACK, callback)
  }

  interface Callback : Serializable {
    fun onPositiveButtonClicked(consumptionElement: ConsumptionElement)
  }

  companion object {
    private const val CALLBACK = "CALLBACK"
  }
}