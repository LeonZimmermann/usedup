package de.usedup.android.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import de.usedup.android.R
import de.usedup.android.components.SimpleProductPreviewAdapter
import de.usedup.android.databinding.OverviewFragmentBinding
import kotlinx.android.synthetic.main.overview_fragment.*

@AndroidEntryPoint
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
    discrepancy_recyclerview.layoutManager = object : LinearLayoutManager(requireContext()) {
      override fun canScrollVertically(): Boolean = false
    }
    viewModel.discrepancyProductList.observe(viewLifecycleOwner, { discrepancyProductList ->
      adapter.productAmountList = discrepancyProductList
    })
    viewModel.discrepancyAdditionalAmount.observe(viewLifecycleOwner, { discrepancyAdditionalAmount ->
      adapter.additionalAmount = discrepancyAdditionalAmount
    })
  }

  companion object {
    fun newInstance() = OverviewFragment()
  }
}
