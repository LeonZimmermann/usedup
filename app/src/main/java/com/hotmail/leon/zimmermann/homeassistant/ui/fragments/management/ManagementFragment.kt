package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.management

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
import com.google.android.material.tabs.TabLayout
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.ui.components.consumption.ConsumptionElementAdapter
import com.hotmail.leon.zimmermann.homeassistant.ui.components.recyclerViewHandler.RecyclerViewHandler
import kotlinx.android.synthetic.main.management_fragment.*
import kotlinx.android.synthetic.main.management_fragment.view.*
import kotlinx.android.synthetic.main.meal_editor_fragment.view.*


class ManagementFragment : Fragment() {
    private lateinit var viewModel: ManagementViewModel
    private lateinit var adapter: ManagementRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ManagementViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.management_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayout()
        initAdapter()
        initRecyclerView()
        initAddButton()
    }

    private fun initTabLayout() {
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> viewModel.mode.value = Mode.PRODUCT
                    1 -> viewModel.mode.value = Mode.TEMPLATE
                    2 -> viewModel.mode.value = Mode.MEAL
                }
            }
        })
    }

    private fun initAdapter() {
        adapter = ManagementRecyclerAdapter(context!!, findNavController()).apply {
            ItemTouchHelper(object : RecyclerViewHandler(this) {
                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                    return makeMovementFlags(0x00, swipeFlags)
                }
            }).attachToRecyclerView(requireView().recycler_view)
        }
        adapter.products = viewModel.products
        adapter.templates = viewModel.templates
        adapter.meals = viewModel.meals
        viewModel.mode.observe(viewLifecycleOwner, Observer { mode ->
            adapter.mode = mode
        })
    }

    private fun initRecyclerView() {

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context!!)
    }

    private fun initAddButton() {
        add_button.setOnClickListener {
            when (adapter.mode) {
                Mode.PRODUCT -> findNavController().navigate(R.id.action_management_fragment_to_product_editor_fragment)
                Mode.TEMPLATE -> findNavController().navigate(R.id.action_management_fragment_to_template_editor_fragment)
                Mode.MEAL -> findNavController().navigate(R.id.action_management_fragment_to_meal_editor_fragment)
            }
        }
    }

    enum class Mode {
        PRODUCT, TEMPLATE, MEAL
    }

    companion object {
        fun newInstance() = ManagementFragment()
    }
}