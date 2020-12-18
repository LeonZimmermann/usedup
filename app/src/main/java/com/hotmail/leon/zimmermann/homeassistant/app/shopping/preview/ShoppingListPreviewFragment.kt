package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.ShoppingListPreviewFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.shopping_list_preview_fragment.*

@AndroidEntryPoint
class ShoppingListPreviewFragment : Fragment() {

  private val viewModel: ShoppingListPreviewViewModel by viewModels()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.shopping_list_preview_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding()
    initAdapters()
  }

  private fun initDatabinding() {
    val binding = ShoppingListPreviewFragmentBinding.bind(requireView())
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
  }

  private fun initAdapters() {
    initAdditionalProductRecyclerAdapter()
    initProductDisrepancyRecyclerAdapter()
    initMealRecyclerAdapter()
  }

  private fun initAdditionalProductRecyclerAdapter() {
    val additionalProductRecyclerAdapter =
      AdditionalProductRecyclerAdapter(requireContext(), viewModel.additionProductRecyclerAdapterCallback)
    additionalProductRecyclerView.adapter = additionalProductRecyclerAdapter
    additionalProductRecyclerView.layoutManager = object: LinearLayoutManager(requireContext()) {
      override fun canScrollVertically(): Boolean = false
    }
    viewModel.shoppingListPreview.observe(viewLifecycleOwner, Observer { shoppingListPreview ->
      additionalProductRecyclerAdapter.setAdditionalProductList(
        shoppingListPreview.additionalProductList.map { AdditionalProductRepresentation(it) })
    })
  }

  private fun initProductDisrepancyRecyclerAdapter() {
    val productDiscrepancyRecyclerAdapter =
      ProductDiscrepancyRecyclerAdapter(requireContext(), viewModel.productDiscrepancyRecyclerAdapterCallback)
    productDiscrepancyRecyclerView.adapter = productDiscrepancyRecyclerAdapter
    productDiscrepancyRecyclerView.layoutManager = object: LinearLayoutManager(requireContext()) {
      override fun canScrollVertically(): Boolean = false
    }
    viewModel.shoppingListPreview.observe(viewLifecycleOwner, Observer { shoppingListPreview ->
      productDiscrepancyRecyclerAdapter.setProductDiscrepancyList(
        shoppingListPreview.productDiscrepancyList.map { ProductDiscrepancyRepresentation(it) })
    })
  }

  private fun initMealRecyclerAdapter() {
    val mealRecyclerAdapter = MealRecyclerAdapter(requireContext())
    mealRecyclerView.adapter = mealRecyclerAdapter
    mealRecyclerView.layoutManager = object: LinearLayoutManager(requireContext()) {
      override fun canScrollVertically(): Boolean = false
    }
    viewModel.shoppingListPreview.observe(viewLifecycleOwner, Observer { shoppingListPreview ->
      mealRecyclerAdapter.setMealList(shoppingListPreview.mealList.map { MealRepresentation(it) })
    })
  }

  companion object {
    fun newInstance() = ShoppingListPreviewFragment()
  }
}