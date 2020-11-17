package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
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

  init {
    viewModelScope.launch(Dispatchers.IO) {
      mutableShoppingListPreview.postValue(
        ShoppingListPreviewGenerator.generateShoppingListPreview(productRepository, mealRepository, plannerRepository))
    }
  }

}