package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.ConsumptionCreationFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.ui.components.recyclerViewHandler.RecyclerViewHandler
import kotlinx.android.synthetic.main.consumption_creation_fragment.view.*

class ConsumptionCreationFragment : Fragment() {

    private lateinit var viewModel: ConsumptionCreationViewModel
    private lateinit var binding: ConsumptionCreationFragmentBinding
    private lateinit var adapter: ConsumptionIngredientsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.consumption_creation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ConsumptionCreationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding = ConsumptionCreationFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.eventHandler = EventHandler()
        adapter = ConsumptionIngredientsAdapter(context!!, ::onIngredientRemoved).apply {
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
        viewModel.consumptionTemplateList.observe(this, Observer { consumptionEntityList ->
            adapter.setConsumptionEntityList(consumptionEntityList)
            val params = view.add_ingredients_button.layoutParams as ViewGroup.MarginLayoutParams
            val topMarginDimensionId = if (consumptionEntityList.isEmpty()) R.dimen.lMargin else R.dimen.sMargin
            params.topMargin = context!!.resources.getDimension(topMarginDimensionId).toInt()
            view.add_ingredients_button.layoutParams = params
        })
        view.ingredients_list.adapter = adapter
        view.ingredients_list.layoutManager = LinearLayoutManager(context)
    }

    private fun onIngredientRemoved(position: Int) {
        val consumptionTemplateList = viewModel.consumptionTemplateList.value!!
        consumptionTemplateList.removeAt(position)
        viewModel.consumptionTemplateList.value = consumptionTemplateList
    }

    inner class EventHandler {

        fun onImageViewClicked(view: View) {
            findNavController().navigate(R.id.action_consumption_creation_fragment_to_consumption_creation_picture_preview_fragment)
        }

        fun onAddIngredientsButtonClicked(view: View) {
            ConsumptionIngredientsDialog().show(fragmentManager!!, "ConsumptionIngredientsDialog")
        }

        fun onSaveDinnerButtonClicked(view: View) {
            viewModel.saveDinnerToDatabase()
        }
    }

    companion object {
        fun newInstance() = ConsumptionCreationFragment()
    }
}
