package com.hotmail.leon.zimmermann.homeassistant.shopping

import android.app.Application
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.product.Product
import com.hotmail.leon.zimmermann.homeassistant.product.ProductDatabase
import com.hotmail.leon.zimmermann.homeassistant.product.ProductRepository
import kotlinx.coroutines.launch

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productList: LiveData<List<Product>>

    init {
        val productDao = ProductDatabase.getDatabase(application, viewModelScope).productDao()
        productRepository = ProductRepository(productDao)
        productList = Transformations.map(productRepository.productList) { list ->
            list.filter { product -> product.discrepancy > 0 }
        }
    }

    fun updateAll(productList: List<Product>) {
        viewModelScope.launch {
            productRepository.updateAll(productList)
        }
    }

}
