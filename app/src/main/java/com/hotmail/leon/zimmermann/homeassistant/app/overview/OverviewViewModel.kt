package com.hotmail.leon.zimmermann.homeassistant.app.overview

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.navigation.Navigation
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product.ProductRepository
import kotlin.math.max

class OverviewViewModel(application: Application) : AndroidViewModel(application) {
  val products: MutableLiveData<MutableList<Product>> = ProductRepository.products

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
