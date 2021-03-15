package de.usedup.android.consumption

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import de.usedup.android.R
import de.usedup.android.databinding.ConsumptionFragmentComponentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.consumption_fragment_component.*

@AndroidEntryPoint
class ConsumptionFragmentComponent : Fragment() {

  private val viewModel: ConsumptionViewModel by viewModels()
  private lateinit var binding: ConsumptionFragmentComponentBinding
  private lateinit var inputMethodManager: InputMethodManager

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.consumption_fragment_component, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    initDatabinding(view)
    initNameInput()
    initQuantityInput()
    initMeasureInput()
    initErrorSnackbar()
  }

  private fun initDatabinding(view: View) {
    binding = ConsumptionFragmentComponentBinding.bind(view)
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
  }

  private fun initNameInput() {
    viewModel.mappedNameList.observe(viewLifecycleOwner, { mappedNameList ->
      mappedNameList.removeObservers(viewLifecycleOwner)
      mappedNameList.observe(viewLifecycleOwner, { nameList ->
        name_input.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, nameList))
      })
    })
    viewModel.nameHint.observe(viewLifecycleOwner, { nameHint ->
      name_input.hint = nameHint
    })
    name_input.onItemClickListener = viewModel
  }

  private fun initQuantityInput() {
    viewModel.quantityInputFieldFocus.observe(viewLifecycleOwner, { focusQuantityInputField ->
      if (focusQuantityInputField) {
        quantity_input.requestFocus()
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        viewModel.quantityInputFieldFocus.postValue(false)
      }
    })
  }

  private fun initMeasureInput() {
    viewModel.measureNameList.observe(viewLifecycleOwner, { measureList ->
      measure_input.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, measureList))
    })
  }

  private fun initErrorSnackbar() {
    viewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
      Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show()
    })
  }
}