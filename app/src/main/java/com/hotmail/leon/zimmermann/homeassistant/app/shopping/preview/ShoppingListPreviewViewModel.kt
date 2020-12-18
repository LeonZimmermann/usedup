package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.app.orElse
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.PlannerRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingListPreviewViewModel @ViewModelInject constructor(
  private val productRepository: ProductRepository,
  private val mealRepository: MealRepository,
  private val plannerRepository: PlannerRepository) : ViewModel() {

  private val mutableShoppingListPreview = MutableLiveData<ShoppingListPreview>()

  val shoppingListPreview: LiveData<ShoppingListPreview> = Transformations.map(mutableShoppingListPreview) { it }

  val productNames: LiveData<List<String>> =
    Transformations.map(productRepository.products) { products -> products.map { it.name }.toList() }
  var shoppingProductDialog: MutableLiveData<ShoppingProductDialog?> = MutableLiveData(null)
  var editShoppingProduct: ShoppingProduct? = null

  init {
    viewModelScope.launch(Dispatchers.IO) {
      mutableShoppingListPreview.postValue(
        ShoppingListPreviewGenerator.generateShoppingListPreview(productRepository, mealRepository, plannerRepository))
    }
  }

  private fun addAdditionalProduct(product: ShoppingProduct) {
    val shoppingListPreviewObject = requireNotNull(shoppingListPreview.value)
    shoppingListPreviewObject.additionalProductList.add(product).orElse {
      shoppingListPreviewObject.additionalProductList.first { it == product }.cartAmount += product.cartAmount
    }
    mutableShoppingListPreview.postValue(shoppingListPreviewObject)
  }

  private fun removeAdditionalProduct(product: ShoppingProduct) {
    val shoppingListPreviewObject = requireNotNull(shoppingListPreview.value)
    shoppingListPreviewObject.additionalProductList.remove(product)
    mutableShoppingListPreview.postValue(shoppingListPreviewObject)
  }

  private fun editDiscrepancyProduct(product: ShoppingProduct, newAmount: Int) {
    val shoppingListPreviewObject = requireNotNull(shoppingListPreview.value)
    product.cartAmount = newAmount
    mutableShoppingListPreview.postValue(shoppingListPreviewObject)
  }

  private fun removeDiscrepancyProduct(product: ShoppingProduct) {
    val shoppingListPreviewObject = requireNotNull(shoppingListPreview.value)
    shoppingListPreviewObject.productDiscrepancyList.remove(product)
    mutableShoppingListPreview.postValue(shoppingListPreviewObject)
  }

  fun onAddProductButtonClicked() {
    editShoppingProduct = null
    shoppingProductDialog.postValue(ShoppingProductDialog(R.string.add_additional_product))
    // TODO Implement
  }

  suspend fun getProductForName(productName: String) = productRepository.getProductForName(productName)

  val additionProductRecyclerAdapterCallback = object : AdditionalProductRecyclerAdapter.Callback {
    override fun onEditButtonClicked(view: View, additionalProductRepresentation: AdditionalProductRepresentation) {
      editShoppingProduct = additionalProductRepresentation.data
      shoppingProductDialog.postValue(ShoppingProductDialog(R.string.change_cart_amount_for_product))
      // TODO Replace
    }

    override fun onRemoveButtonClicked(view: View, additionalProductRepresentation: AdditionalProductRepresentation) {
      removeAdditionalProduct(additionalProductRepresentation.data)
    }
  }

  val productDiscrepancyRecyclerAdapterCallback = object : ProductDiscrepancyRecyclerAdapter.Callback {
    override fun onEditButtonClicked(view: View, productDiscrepancyRepresentation: ProductDiscrepancyRepresentation) {
      editShoppingProduct = productDiscrepancyRepresentation.data
      shoppingProductDialog.postValue(ShoppingProductDialog(R.string.change_cart_amount_for_product))
      // TODO Replace
    }

    override fun onRemoveButtonClicked(view: View, productDiscrepancyRepresentation: ProductDiscrepancyRepresentation) {
      removeDiscrepancyProduct(productDiscrepancyRepresentation.data)
    }
  }
}