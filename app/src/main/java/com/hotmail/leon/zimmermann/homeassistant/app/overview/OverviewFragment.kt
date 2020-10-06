package com.hotmail.leon.zimmermann.homeassistant.app.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.components.SimpleProductPreviewAdapter
import com.hotmail.leon.zimmermann.homeassistant.databinding.OverviewFragmentBinding
import kotlinx.android.synthetic.main.overview_fragment.*

class OverviewFragment : Fragment() {

  private val viewModel: OverviewViewModel by viewModels()
  private lateinit var binding: OverviewFragmentBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.overview_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding(view)
    initDiscrepancyCard()
  }

  private fun initDatabinding(view: View) {
    binding = OverviewFragmentBinding.bind(view)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
  }

  private fun initDiscrepancyCard() {
    val adapter = SimpleProductPreviewAdapter(requireContext(), discrepancy_recyclerview)
    discrepancy_recyclerview.adapter = adapter
    discrepancy_recyclerview.layoutManager = LinearLayoutManager(requireContext())
    viewModel.discrepancyProductList.observe(viewLifecycleOwner, Observer { discrepancyProductList ->
      adapter.productAmountList = discrepancyProductList
    })
    viewModel.discrepancyAdditionalAmount.observe(viewLifecycleOwner, Observer { discrepancyAdditionalAmount ->
      adapter.additionalAmount = discrepancyAdditionalAmount
    })
  }

  companion object {
    fun newInstance() = OverviewFragment()
  }
}
