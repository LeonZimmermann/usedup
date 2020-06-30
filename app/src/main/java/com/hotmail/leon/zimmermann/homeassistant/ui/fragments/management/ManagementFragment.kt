package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.management

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.management_fragment.*


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
        initRecyclerView()
    }

    private fun initTabLayout() {
        tab_layout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> adapter.mode = ManagementRecyclerAdapter.Mode.PRODUCT
                    1 -> adapter.mode = ManagementRecyclerAdapter.Mode.TEMPLATE
                    2 -> adapter.mode = ManagementRecyclerAdapter.Mode.MEAL
                }
            }
        })
    }

    private fun initRecyclerView() {
        adapter = ManagementRecyclerAdapter(context!!, findNavController())
        adapter.products = viewModel.products
        adapter.templates = viewModel.templates
        adapter.meals = viewModel.meals
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context!!)
    }

    companion object {
        fun newInstance() = ManagementFragment()
    }
}