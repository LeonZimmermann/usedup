package de.usedup.android.management.templates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import de.usedup.android.R
import de.usedup.android.components.consumption.ConsumptionElement
import de.usedup.android.components.consumption.ConsumptionElementAdapter
import de.usedup.android.components.consumption.ConsumptionElementDialogFragment
import de.usedup.android.components.recyclerViewHandler.RecyclerViewHandler
import de.usedup.android.databinding.TemplateEditorFragmentBinding
import de.usedup.android.datamodel.api.objects.Id
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.meal_editor_fragment.view.ingredients_list
import kotlinx.android.synthetic.main.template_editor_fragment.view.*

@AndroidEntryPoint
class TemplateEditorFragment : Fragment() {

  private val viewModel: TemplateEditorViewModel by viewModels()
  private lateinit var binding: TemplateEditorFragmentBinding
  private lateinit var adapter: ConsumptionElementAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initTemplateId()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.template_editor_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding(view)
    initAdapter(view)
    initIngredientsList(view)
    initAddComponentButton(view)
    initErrorMessage()
  }

  private fun initTemplateId() {
    arguments?.apply {
      val templateId = getSerializable(TEMPLATE_ID) as? Id?
      templateId?.let { viewModel.setTemplateId(it) }
    }
  }

  private fun initDatabinding(view: View) {
    binding = TemplateEditorFragmentBinding.bind(view)
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
  }

  private fun initAdapter(view: View) {
    adapter = ConsumptionElementAdapter(requireContext(), viewModel::onConsumptionElementRemoved).apply {
      ItemTouchHelper(object : RecyclerViewHandler(this) {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
          val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
          return makeMovementFlags(0x00, swipeFlags)
        }
      }).attachToRecyclerView(view.ingredients_list)
    }
    viewModel.consumptionElementList.observe(viewLifecycleOwner, { consumptionElementList ->
      adapter.setConsumptionElementList(consumptionElementList)
    })
  }

  private fun initIngredientsList(view: View) {
    view.ingredients_list.adapter = adapter
    view.ingredients_list.layoutManager = LinearLayoutManager(context)
  }

  private fun initAddComponentButton(view: View) {
    view.add_component_button.setOnClickListener {
      ConsumptionElementDialogFragment(object: ConsumptionElementDialogFragment.Callback {
        override fun onPositiveButtonClicked(consumptionElement: ConsumptionElement) {
          viewModel.addConsumptionElement(consumptionElement)
        }
      })
        .show(parentFragmentManager, "ConsumptionElementDialog")
    }
  }

  private fun initErrorMessage() {
    viewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
      Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show()
    })
  }

  companion object {
    const val TEMPLATE_ID = "templateId"

    fun newInstance() = TemplateEditorFragment()
  }
}