package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.browser

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.consumption_browser_fragment.*

class ConsumptionBrowserFragment : Fragment() {

    companion object {
        fun newInstance() = ConsumptionBrowserFragment()
    }

    private lateinit var viewModel: ConsumptionBrowserViewModel
    private lateinit var adapter: ConsumptionBrowserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.consumption_browser_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ConsumptionBrowserViewModel::class.java)
        adapter = ConsumptionBrowserListAdapter(context!!, View.OnClickListener { onItemClicked(it) })
        consumption_browser_list.adapter = adapter
        consumption_browser_list.layoutManager = LinearLayoutManager(context!!)
        viewModel.consumptionLists.observe(this, Observer {
            adapter.setConsumptionLists(it)
        })
        consumption_browser_add_button.setOnClickListener { findNavController().navigate(R.id.action_consumption_browser_fragment_to_consumption_creation_fragment) }
    }

    private fun onItemClicked(view: View) {
        findNavController().navigate(
            R.id.action_consumption_browser_fragment_to_consumption_details,
            bundleOf("editConsumptionList" to adapter[consumption_browser_list.getChildAdapterPosition(view)])
        )
    }
}
