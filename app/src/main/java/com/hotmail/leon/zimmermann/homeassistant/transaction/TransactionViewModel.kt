package com.hotmail.leon.zimmermann.homeassistant.transaction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.product.Product
import com.hotmail.leon.zimmermann.homeassistant.product.ProductDatabase
import com.hotmail.leon.zimmermann.homeassistant.product.ProductRepository
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository
    val productList: LiveData<List<Product>>

    init {
        val productDao = ProductDatabase.getDatabase(application, viewModelScope).productDao()
        repository = ProductRepository(productDao)
        productList = repository.productList
    }

    fun update(product: Product) {
        viewModelScope.launch {
            repository.update(product)
        }
    }

}
