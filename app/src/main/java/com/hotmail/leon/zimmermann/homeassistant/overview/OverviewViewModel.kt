package com.hotmail.leon.zimmermann.homeassistant.overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.product.Product
import com.hotmail.leon.zimmermann.homeassistant.product.ProductDatabase
import com.hotmail.leon.zimmermann.homeassistant.product.ProductRepository

class OverviewViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productList: LiveData<List<Product>>

    init {
        val productDao = ProductDatabase.getDatabase(application, viewModelScope).productDao()
        productRepository = ProductRepository(productDao)
        productList = Transformations.map(productRepository.productList) { list ->
            list.filter { it.discrepancy > 0 }
        }
    }
}
