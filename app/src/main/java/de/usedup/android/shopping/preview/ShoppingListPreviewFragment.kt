package de.usedup.android.shopping.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import de.usedup.android.R
import de.usedup.android.databinding.ShoppingListPreviewFragmentBinding
import kotlinx.android.synthetic.main.shopping_list_preview_fragment.*

@AndroidEntryPoint
class ShoppingListPreviewFragment : Fragment() {

  private val viewModel: ShoppingListPreviewViewModel by viewModels()

  private lateinit var additionalProductRecyclerAdapter: AdditionalProductRecyclerAdapter
  private lateinit var productDiscrepancyRecyclerAdapter: ProductDiscrepancyRecyclerAdapter
  private lateinit var mealRecyclerAdapter: MealRecyclerAdapter

  override fun onResume() {
    super.onResume()
    viewModel.initShoppingListPreview()
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
    additionalProductRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    viewModel.shoppingListPreview.observe(viewLifecycleOwner, { shoppingListPreview ->
      additionalProductRecyclerAdapter.initAdditionalProductList(
        shoppingListPreview.additionalProductList.map { AdditionalProductRepresentation(it) })
    })
  }

  private fun initProductDisrepancyRecyclerAdapter() {
    productDiscrepancyRecyclerAdapter =
      ProductDiscrepancyRecyclerAdapter(requireContext(), viewModel.productDiscrepancyRecyclerAdapterCallback)
    productDiscrepancyRecyclerView.adapter = productDiscrepancyRecyclerAdapter
    productDiscrepancyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    viewModel.shoppingListPreview.observe(viewLifecycleOwner, { shoppingListPreview ->
      productDiscrepancyRecyclerAdapter.initProductDiscrepancyList(
        shoppingListPreview.productDiscrepancyList.map { ProductDiscrepancyRepresentation(it) })
    })
  }

  private fun initMealRecyclerAdapter() {
    mealRecyclerAdapter = MealRecyclerAdapter(requireContext(), viewModel.viewModelScope)
    mealRecyclerView.adapter = mealRecyclerAdapter
    mealRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    viewModel.shoppingListPreview.observe(viewLifecycleOwner, { shoppingListPreview ->
      mealRecyclerAdapter.setMealList(shoppingListPreview.mealList.map { MealRepresentation(it) })
    })
  }

  private fun initShoppingProductDialogHandler() {
    viewModel.shoppingProductDialog.observe(viewLifecycleOwner, {
      it?.show(parentFragmentManager, SHOPPING_PRODUCT_DIALOG)
    })
  }

  private fun initAddAdditionalProductShoppingProductHandler() {
    viewModel.addAdditionalProductShoppingProduct.observe(viewLifecycleOwner, { shoppingProduct ->
      shoppingProduct?.let {
        additionalProductRecyclerAdapter.addAdditionalProduct(AdditionalProductRepresentation(it))
        viewModel.addAdditionalProductShoppingProduct.postValue(null)
      }
    })
  }

  private fun initEditAdditionalProductShoppingProductHandler() {
    viewModel.editAdditionalProductShoppingProduct.observe(viewLifecycleOwner, { shoppingProduct ->
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