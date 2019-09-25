package com.hotmail.leon.zimmermann.homeassistant.ui.overview

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.overview_fragment.*

class OverviewFragment : Fragment() {

    companion object {
        fun newInstance() = OverviewFragment()
    }

    private lateinit var viewModel: OverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.overview_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)
        initializeTransactionCard()
        initializeDiscrepancyCard()
        initializeManagementCard()
    }

    private fun initializeTransactionCard() {
        consumption_view_button.setOnClickListener {
            findNavController().navigate(R.id.action_global_transaction_fragment)
        }

        consumption_button.setOnClickListener {
            // TODO Implement
            Toast.makeText(context, "TODO", Toast.LENGTH_LONG).show()
        }
    }

    private fun initializeDiscrepancyCard() {
        val adapter = DiscrepancyListAdapter(context!!)
        overview_discrepancy_container.adapter = adapter
        overview_discrepancy_container.layoutManager = LinearLayoutManager(context!!)
        viewModel.productEntityList.observe(this, Observer {
            it?.let { adapter.setProductList(it) }
        })
        shopping_button.setOnClickListener {
            findNavController().navigate(R.id.action_global_shopping_fragment)
        }
    }

    private fun initializeManagementCard() {
        management_button.setOnClickListener {
            findNavController().navigate(R.id.action_global_management_fragment)
        }
    }

}
