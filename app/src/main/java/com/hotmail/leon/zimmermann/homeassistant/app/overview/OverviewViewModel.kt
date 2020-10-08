package com.hotmail.leon.zimmermann.homeassistant.app.overview

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import kotlin.math.max

class OverviewViewModel @ViewModelInject constructor(productRepository: ProductRepository) : ViewModel() {

  val products: MutableLiveData<MutableList<Product>> = productRepository.products

  val discrepancyProductList: LiveData<List<Pair<Product, Int>>> = Transformations.map(products) { products ->
    products.filter { it.discrepancy > 0 }
      .filterIndexed { index, _ -> index < DISCREPANCY_LIST_SIZE }
      .map { Pair(it, it.discrepancy) }
  }

  val discrepancyAdditionalAmount: LiveData<Int> = Transformations.map(products) { products ->
    max(0, products.filter { it.discrepancy > 0 }.size - DISCREPANCY_LIST_SIZE)
  }

  fun onShoppingButtonClicked(view: View) {
    Navigation.findNavController(view).navigate(R.id.action_global_shopping_fragment)
  }

  companion object {
    private const val DISCREPANCY_LIST_SIZE = 5
  }
}
