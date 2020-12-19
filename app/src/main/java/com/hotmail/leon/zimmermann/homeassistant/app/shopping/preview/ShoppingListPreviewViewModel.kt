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

  val addShoppingProduct: MutableLiveData<ShoppingProduct?> = MutableLiveData(null)

  // Dialog
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

  fun onAddProductButtonClicked() {
    editShoppingProduct = null
    shoppingProductDialog.postValue(
      ShoppingProductDialog(R.string.add_additional_product, object : ShoppingProductDialog.Callback {
        override fun onResult(shoppingProduct: ShoppingProduct) {
          /*
          TODO Change Implementation
          shoppingListPreviewObject.additionalProductList.add(product).orElse {
        shoppingListPreviewObject.additionalProductList.first { it == product }.cartAmount += product.cartAmount
      }
           */
          addShoppingProduct.postValue(shoppingProduct)
        }
      }))
  }

  val additionProductRecyclerAdapterCallback = object : AdditionalProductRecyclerAdapter.Callback {
    override fun onEditButtonClicked(adapter: AdditionalProductRecyclerAdapter, index: Int, view: View,
      additionalProductRepresentation: AdditionalProductRepresentation) {
      editShoppingProduct = additionalProductRepresentation.data
      shoppingProductDialog.postValue(
        ShoppingProductDialog(R.string.change_cart_amount_for_product, object : ShoppingProductDialog.Callback {
          override fun onResult(shoppingProduct: ShoppingProduct) {
            adapter.replaceAdditionalProduct(index, AdditionalProductRepresentation(shoppingProduct))
          }
        }))
    }

    override fun onRemoveButtonClicked(adapter: AdditionalProductRecyclerAdapter, index: Int, view: View,
      additionalProductRepresentation: AdditionalProductRepresentation) {
      adapter.removeAdditionalProduct(index)
    }
  }

  val productDiscrepancyRecyclerAdapterCallback = object : ProductDiscrepancyRecyclerAdapter.Callback {
    override fun onEditButtonClicked(adapter: ProductDiscrepancyRecyclerAdapter, index: Int, view: View,
      productDiscrepancyRepresentation: ProductDiscrepancyRepresentation) {
      editShoppingProduct = productDiscrepancyRepresentation.data
      shoppingProductDialog.postValue(
        ShoppingProductDialog(R.string.change_cart_amount_for_product, object : ShoppingProductDialog.Callback {
          override fun onResult(shoppingProduct: ShoppingProduct) {
            adapter.replaceProductDiscrepancy(index, ProductDiscrepancyRepresentation(shoppingProduct))
          }
        }))
    }

    override fun onRemoveButtonClicked(adapter: ProductDiscrepancyRecyclerAdapter, index: Int, view: View,
      productDiscrepancyRepresentation: ProductDiscrepancyRepresentation) {
      adapter.removeProductDiscrepancy(index)
    }
  }

  suspend fun getProductForName(productName: String) = productRepository.getProductForName(productName)
}