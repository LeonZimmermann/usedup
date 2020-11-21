package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import android.view.View
import android.widget.AdapterView
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
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

  val mutableAddProductNameText: MutableLiveData<String> = MutableLiveData("")
  val mutableAddProductCartAmountText: MutableLiveData<String> = MutableLiveData("")

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
    viewModelScope.launch(Dispatchers.Default) {
      // TODO Perhaps handle these constraint differently (different exception or other form of validation system)
      // TODO Don't throw IllegalArgumentException and handle null case differently
      val productName = mutableAddProductNameText.value ?: throw IllegalArgumentException()
      val cartAmount = mutableAddProductCartAmountText.value?.toInt() ?: throw IllegalArgumentException()
      if (cartAmount <= 0) throw IllegalArgumentException("$cartAmount cannot be negative")
      val product = productRepository.getProductForName(productName)
      addAdditionalProduct(ShoppingProduct(product, cartAmount))
    }
  }

  val addProductNameTextOnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
      val productName = requireNotNull(productNames.value)[position]
      mutableAddProductNameText.postValue(productName)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
      mutableAddProductNameText.postValue("")
    }
  }

  val additionProductRecyclerAdapterCallback = object: AdditionalProductRecyclerAdapter.Callback {
    override fun onEditButtonClicked(view: View, additionalProductRepresentation: AdditionalProductRepresentation) {
      TODO("not implemented")
    }

    override fun onRemoveButtonClicked(view: View, additionalProductRepresentation: AdditionalProductRepresentation) {
      removeAdditionalProduct(additionalProductRepresentation.data)
    }

  }
}