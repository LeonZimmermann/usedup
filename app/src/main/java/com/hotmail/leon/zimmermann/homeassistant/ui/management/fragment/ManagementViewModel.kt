package com.hotmail.leon.zimmermann.homeassistant.ui.management.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductRepository
import kotlinx.coroutines.launch

class ManagementViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productEntityList: LiveData<List<ProductEntity>>

    init {
        val productDao = HomeAssistantDatabase.getDatabase(application, viewModelScope).productDao()
        productRepository = ProductRepository(productDao)
        productEntityList = productRepository.productEntityList
    }

    fun insert(productEntity: ProductEntity)  {
        viewModelScope.launch {
            productRepository.insert(productEntity)
        }
    }

    fun update(productEntity: ProductEntity) {
        viewModelScope.launch {
            productRepository.update(productEntity)
        }
    }

    fun delete(productEntity: ProductEntity) {
        viewModelScope.launch {
            productRepository.delete(productEntity)
        }
    }
}
