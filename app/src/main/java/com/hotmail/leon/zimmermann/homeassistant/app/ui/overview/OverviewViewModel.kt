package com.hotmail.leon.zimmermann.homeassistant.app.ui.overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository

class OverviewViewModel(application: Application) : AndroidViewModel(application) {
    val products: MutableLiveData<List<Product>> = MutableLiveData(ProductRepository.products)

    val discrepancyProductList: LiveData<List<Pair<Product, Int>>> = Transformations.map(products) { products ->
        products.filter { it.discrepancy > 0 }
            .filterIndexed { index, _ -> index < DISCREPANCY_LIST_SIZE }
            .map { Pair(it, it.discrepancy) }
    }

    val discrepancyAdditionalAmount: LiveData<Int> = Transformations.map(products) { products ->
        products.filter { it.discrepancy > 0 }.size - DISCREPANCY_LIST_SIZE
    }

    companion object {
        private const val DISCREPANCY_LIST_SIZE = 5
    }
}
