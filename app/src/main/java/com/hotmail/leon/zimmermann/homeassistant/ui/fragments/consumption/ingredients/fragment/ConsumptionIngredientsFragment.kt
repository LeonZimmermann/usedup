package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.ingredients.fragment

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.ConsumptionIngredientsFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.ingredients.dialog.ConsumptionIngredientsAddDialogFragment
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.ingredients.dialog.ConsumptionIngredientsEditDialogFragment
import kotlinx.android.synthetic.main.consumption_ingredients_fragment.*

class ConsumptionIngredientsFragment : Fragment() {

    private lateinit var viewModel: ConsumptionIngredientsViewModel
    private lateinit var binding: ConsumptionIngredientsFragmentBinding
    private lateinit var adapter: ConsumptionIngredientsBatchListAdapter
    private var eventHandler = EventHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            discardIngredients()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.consumption_ingredients_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ConsumptionIngredientsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding = ConsumptionIngredientsFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.eventHandler = eventHandler
        initializeConsumptionList()
    }

    private fun initializeConsumptionList() {
        adapter =
            ConsumptionIngredientsBatchListAdapter(
                context!!,
                View.OnClickListener { eventHandler.onItemClicked(it) })
        val layoutManager = LinearLayoutManager(context!!)
        val divider = DividerItemDecoration(ingredients_list.context!!, layoutManager.orientation)
        divider.setDrawable(context!!.getDrawable(R.drawable.divider)!!)
        ingredients_list.addItemDecoration(divider)
        ingredients_list.adapter = adapter
        ingredients_list.layoutManager = layoutManager
        viewModel.editConsumptionList.observe(this, Observer { consumptionList ->
            adapter.setConsumptionList(consumptionList)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.ingredients_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.discard_option -> {
            discardIngredients()
            findNavController().navigateUp()
            true
        }
        R.id.submit_option -> {
            submitIngredients()
            findNavController().navigateUp()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun discardIngredients() {
        viewModel.editConsumptionList.value = mutableListOf<ConsumptionIngredientsTemplate>().apply {
            if (viewModel.consumptionList.value != null)
                addAll(viewModel.consumptionList.value!!)
        }
    }

    private fun submitIngredients() {
        viewModel.consumptionList.value = mutableListOf<ConsumptionIngredientsTemplate>().apply {
            if (viewModel.editConsumptionList.value != null)
                addAll(viewModel.editConsumptionList.value!!)
        }
    }

    inner class EventHandler {

        fun onAddButtonClicked(view: View) {
            ConsumptionIngredientsAddDialogFragment.newInstance()
                .show(fragmentManager!!, "AddDialog")
        }

        fun onItemClicked(view: View) {
            val item = adapter[ingredients_list.indexOfChild(view)]
            ConsumptionIngredientsEditDialogFragment.newInstance(item)
                .show(fragmentManager!!, "EditDialog")
        }

    }

    companion object {
        fun newInstance() =
            ConsumptionIngredientsFragment()
    }
}
