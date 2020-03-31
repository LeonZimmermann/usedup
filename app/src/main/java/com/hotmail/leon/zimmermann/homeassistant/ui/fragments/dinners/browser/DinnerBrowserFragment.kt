package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.dinners.browser

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
import com.hotmail.leon.zimmermann.homeassistant.databinding.DinnerBrowserFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.ui.components.picker.DinnerListAdapter
import kotlinx.android.synthetic.main.dinner_browser_fragment.*

class DinnerBrowserFragment : Fragment() {

    private lateinit var viewModel: DinnerBrowserViewModel
    private lateinit var binding: DinnerBrowserFragmentBinding
    private lateinit var adapter: DinnerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DinnerBrowserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dinner_browser_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatabinding(view)
        initList()
    }

    private fun initDatabinding(view: View) {
        binding = DinnerBrowserFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initList() {
        adapter = DinnerListAdapter(
            context!!,
            View.OnClickListener { onItemClicked(it) })
        consumption_browser_list.adapter = adapter
        consumption_browser_list.layoutManager = LinearLayoutManager(context!!)
        viewModel.consumptionLists.observe(this, Observer {
            adapter.setConsumptionLists(it)
        })
    }

    private fun onItemClicked(view: View) {
        // TODO Implement
    }

    companion object {
        fun newInstance() = DinnerBrowserFragment()
    }
}
