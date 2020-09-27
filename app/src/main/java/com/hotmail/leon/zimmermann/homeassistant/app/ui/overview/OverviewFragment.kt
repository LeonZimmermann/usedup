package com.hotmail.leon.zimmermann.homeassistant.app.ui.overview

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.overview_fragment.*

class OverviewFragment : Fragment() {

    private lateinit var viewModel: OverviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.overview_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDiscrepancyCard()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)
    }

    private fun initDiscrepancyCard() {
        val adapter = com.hotmail.leon.zimmermann.homeassistant.components.SimpleProductPreviewAdapter(
            context!!,
            discrepancy_recyclerview
        )
        discrepancy_recyclerview.adapter = adapter
        discrepancy_recyclerview.layoutManager = LinearLayoutManager(context!!)
        viewModel.discrepancyProductList.observe(viewLifecycleOwner, Observer { discrepancyProductList ->
            adapter.productAmountList = discrepancyProductList
        })
        viewModel.discrepancyAdditionalAmount.observe(viewLifecycleOwner, Observer { discrepancyAdditionalAmount ->
            adapter.additionalAmount = discrepancyAdditionalAmount
        })
        shopping_button.setOnClickListener {
            findNavController().navigate(R.id.action_global_shopping_fragment)
        }
    }

    companion object {
        fun newInstance() = OverviewFragment()
    }
}
