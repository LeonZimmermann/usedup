package com.hotmail.leon.zimmermann.homeassistant.management.dialog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.product.Product
import com.hotmail.leon.zimmermann.homeassistant.product.ProductDatabase
import com.hotmail.leon.zimmermann.homeassistant.product.ProductRepository
import kotlinx.coroutines.launch

class ManagementItemDialogViewModel(application: Application): AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productList: LiveData<List<Product>>

    init {
        val productDao = ProductDatabase.getDatabase(application, viewModelScope).productDao()
        productRepository = ProductRepository(productDao)
        productList = productRepository.productList
    }

    fun insert(product: Product)  {
        viewModelScope.launch {
            productRepository.insert(product)
        }
    }

    fun update(product: Product) {
        viewModelScope.launch {
            productRepository.update(product)
        }
    }

    fun delete(product: Product) {
        viewModelScope.launch {
            productRepository.delete(product)
        }
    }

}