package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.ShoppingListPreviewFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.shopping_list_preview_fragment.*

@AndroidEntryPoint
class ShoppingListPreviewFragment : Fragment() {

  private lateinit var viewModel: ShoppingListPreviewViewModel

  private lateinit var additionalProductRecyclerAdapter: AdditionalProductRecyclerAdapter
  private lateinit var productDiscrepancyRecyclerAdapter: ProductDiscrepancyRecyclerAdapter
  private lateinit var mealRecyclerAdapter: MealRecyclerAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(requireActivity())[ShoppingListPreviewViewModel::class.java]
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.shopping_list_preview_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding()
    initAdditionalProductRecyclerAdapter()
    initProductDisrepancyRecyclerAdapter()
    initMealRecyclerAdapter()
    initShoppingProductDialogHandler()
    initAddAdditionalProductShoppingProductHandler()
    initEditAdditionalProductShoppingProductHandler()
  }

  private fun initDatabinding() {
    val binding = ShoppingListPreviewFragmentBinding.bind(requireView())
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
  }

  private fun initAdditionalProductRecyclerAdapter() {
    additionalProductRecyclerAdapter =
      AdditionalProductRecyclerAdapter(requireContext(), viewModel.additionProductRecyclerAdapterCallback)
    additionalProductRecyclerView.adapter = additionalProductRecyclerAdapter
    additionalProductRecyclerView.layoutManager = object : LinearLayoutManager(requireContext()) {
      override fun canScrollVertically(): Boolean = false
    }
    viewModel.shoppingListPreview.observe(viewLifecycleOwner, Observer { shoppingListPreview ->
      additionalProductRecyclerAdapter.initAdditionalProductList(
        shoppingListPreview.additionalProductList.map { AdditionalProductRepresentation(it) })
    })
  }

  private fun initProductDisrepancyRecyclerAdapter() {
    productDiscrepancyRecyclerAdapter =
      ProductDiscrepancyRecyclerAdapter(requireContext(), viewModel.productDiscrepancyRecyclerAdapterCallback)
    productDiscrepancyRecyclerView.adapter = productDiscrepancyRecyclerAdapter
    productDiscrepancyRecyclerView.layoutManager = object : LinearLayoutManager(requireContext()) {
      override fun canScrollVertically(): Boolean = false
    }
    viewModel.shoppingListPreview.observe(viewLifecycleOwner, Observer { shoppingListPreview ->
      productDiscrepancyRecyclerAdapter.initProductDiscrepancyList(
        shoppingListPreview.productDiscrepancyList.map { ProductDiscrepancyRepresentation(it) })
    })
  }

  private fun initMealRecyclerAdapter() {
    mealRecyclerAdapter = MealRecyclerAdapter(requireContext())
    mealRecyclerView.adapter = mealRecyclerAdapter
    mealRecyclerView.layoutManager = object : LinearLayoutManager(requireContext()) {
      override fun canScrollVertically(): Boolean = false
    }
    viewModel.shoppingListPreview.observe(viewLifecycleOwner, Observer { shoppingListPreview ->
      mealRecyclerAdapter.setMealList(shoppingListPreview.mealList.map { MealRepresentation(it) })
    })
  }

  private fun initShoppingProductDialogHandler() {
    viewModel.shoppingProductDialog.observe(viewLifecycleOwner, Observer {
      it?.show(parentFragmentManager, SHOPPING_PRODUCT_DIALOG)
    })
  }

  private fun initAddAdditionalProductShoppingProductHandler() {
    viewModel.addAdditionalProductShoppingProduct.observe(viewLifecycleOwner, Observer { shoppingProduct ->
      shoppingProduct?.let {
        additionalProductRecyclerAdapter.addAdditionalProduct(AdditionalProductRepresentation(it))
        viewModel.addAdditionalProductShoppingProduct.postValue(null)
      }
    })
  }

  private fun initEditAdditionalProductShoppingProductHandler() {
    viewModel.editAdditionalProductShoppingProduct.observe(viewLifecycleOwner, Observer { shoppingProduct ->
      shoppingProduct?.let {
        additionalProductRecyclerAdapter.replaceAdditionalProduct(AdditionalProductRepresentation(it))
        viewModel.editAdditionalProductShoppingProduct.postValue(null)
      }
    })
  }

  companion object {
    private const val SHOPPING_PRODUCT_DIALOG = "SHOPPING_PRODUCT_DIALOG"

    fun newInstance() = ShoppingListPreviewFragment()
  }
}