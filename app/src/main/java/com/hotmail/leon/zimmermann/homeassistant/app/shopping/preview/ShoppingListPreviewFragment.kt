package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
    initAdditionalProductNameInput()
    initAdapters()
  }

  private fun initDatabinding() {
    val binding = ShoppingListPreviewFragmentBinding.bind(requireView())
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
  }

  private fun initAdditionalProductNameInput() {
    additional_product_name_input.onItemSelectedListener = viewModel.addProductNameTextOnItemSelectedListener
    viewModel.productNames.observe(viewLifecycleOwner, Observer { productNames ->
      additional_product_name_input.setAdapter(
        ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, productNames))
    })
  }

  private fun initAdapters() {
    val additionalProductRecyclerAdapter =
      AdditionalProductRecyclerAdapter(requireContext(), viewModel.additionProductRecyclerAdapterCallback)
    additionalProductRecyclerView.adapter = additionalProductRecyclerAdapter
    additionalProductRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    val productDiscrepancyRecyclerAdapter =
      ProductDiscrepancyRecyclerAdapter(requireContext(), viewModel.productDiscrepancyRecyclerAdapterCallback)
    productDiscrepancyRecyclerView.adapter = productDiscrepancyRecyclerAdapter
    productDiscrepancyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    val mealRecyclerAdapter = MealRecyclerAdapter(requireContext())
    mealRecyclerView.adapter = mealRecyclerAdapter
    mealRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    viewModel.shoppingListPreview.observe(viewLifecycleOwner, Observer { shoppingListPreview ->
      additionalProductRecyclerAdapter.setAdditionalProductList(
        shoppingListPreview.additionalProductList.map { AdditionalProductRepresentation(it) })
      productDiscrepancyRecyclerAdapter.setProductDiscrepancyList(
        shoppingListPreview.productDiscrepancyList.map { ProductDiscrepancyRepresentation(it) })
      mealRecyclerAdapter.setMealList(shoppingListPreview.mealList.map { MealRepresentation(it) })
    })
  }

  companion object {
    fun newInstance() = ShoppingListPreviewFragment()
  }
}