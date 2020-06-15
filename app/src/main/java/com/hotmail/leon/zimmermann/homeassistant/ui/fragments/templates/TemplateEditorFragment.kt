package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.templates

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.MealEditorFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.databinding.TemplateEditorFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.ui.components.consumption.ConsumptionElementAdapter
import com.hotmail.leon.zimmermann.homeassistant.ui.components.consumption.ConsumptionElementDialog
import com.hotmail.leon.zimmermann.homeassistant.ui.components.recyclerViewHandler.RecyclerViewHandler
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.editor.MealEditorFragment
import kotlinx.android.synthetic.main.meal_editor_fragment.view.*
import kotlinx.android.synthetic.main.meal_editor_fragment.view.ingredients_list
import kotlinx.android.synthetic.main.template_editor_fragment.view.*
import java.io.File
import kotlin.concurrent.fixedRateTimer

class TemplateEditorFragment : Fragment() {

    private lateinit var viewModel: TemplateEditorViewModel
    private lateinit var binding: TemplateEditorFragmentBinding
    private lateinit var adapter: ConsumptionElementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TemplateEditorViewModel::class.java)
        arguments?.apply {
            val templateId = getSerializable(TEMPLATE_ID) as? String?
            templateId?.let { viewModel.setTemplateId(it) }
        }
        savedInstanceState?.apply {
            val templateId = getSerializable(TEMPLATE_ID) as? String?
            templateId?.let { viewModel.setTemplateId(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.template_editor_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatabinding(view)
        adapter = ConsumptionElementAdapter(
            context!!,
            ::onComponentRemoved
        ).apply {
            ItemTouchHelper(object : RecyclerViewHandler(this) {
                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                    return makeMovementFlags(0x00, swipeFlags)
                }
            }).attachToRecyclerView(view.ingredients_list)
        }
        viewModel.consumptionElementList.observe(viewLifecycleOwner, Observer { consumptionElementList ->
            adapter.setConsumptionElementList(consumptionElementList)
            val params = view.add_component_button.layoutParams as ViewGroup.MarginLayoutParams
            val topMarginDimensionId = if (consumptionElementList.isEmpty()) R.dimen.lMargin else R.dimen.sMargin
            params.topMargin = context!!.resources.getDimension(topMarginDimensionId).toInt()
            view.add_component_button.layoutParams = params
        })
        view.ingredients_list.adapter = adapter
        view.ingredients_list.layoutManager = LinearLayoutManager(context)
    }

    private fun initDatabinding(view: View) {
        binding = TemplateEditorFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.eventHandler = EventHandler()
    }

    private fun onComponentRemoved(position: Int) {
        val consumptionTemplateList = viewModel.consumptionElementList.value!!
        consumptionTemplateList.removeAt(position)
        viewModel.consumptionElementList.value = consumptionTemplateList
    }

    inner class EventHandler {
        fun onAddComponentButtonClicked(view: View) {
            ConsumptionElementDialog { viewModel.addConsumptionElement(it) }.show(
                fragmentManager!!,
                "ConsumptionElementDialog"
            )
        }

        fun onSaveTemplateButtonClicked(view: View) {
            viewModel.saveTemplateToDatabase()
            findNavController().navigateUp()
        }
    }

    companion object {
        const val TEMPLATE_ID = "templateId"

        fun newInstance() = TemplateEditorFragment()
    }
}