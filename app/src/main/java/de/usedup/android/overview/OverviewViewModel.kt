package de.usedup.android.overview

import android.view.View
import androidx.lifecycle.*
import androidx.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.datamodel.api.objects.Product
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import de.usedup.android.R
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class OverviewViewModel @Inject constructor(productRepository: ProductRepository) : ViewModel() {

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
