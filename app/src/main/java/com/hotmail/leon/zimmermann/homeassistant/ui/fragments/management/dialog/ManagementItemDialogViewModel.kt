package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.management.dialog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductRepository
import kotlinx.coroutines.launch

class ManagementItemDialogViewModel(application: Application): AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productEntityList: LiveData<List<ProductEntity>>

    init {
        val database = HomeAssistantDatabase.getDatabase(application, viewModelScope)

        val productDao = database.productDao()
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