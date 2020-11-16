package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.PlannerRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository

class ShoppingListPreviewViewModel @ViewModelInject constructor(productRepository: ProductRepository,
  plannerRepository: PlannerRepository) : ViewModel() {
  val shoppingListPreview =
    MutableLiveData(ShoppingListPreviewGenerator.generateShoppingListPreview(productRepository, plannerRepository))
}