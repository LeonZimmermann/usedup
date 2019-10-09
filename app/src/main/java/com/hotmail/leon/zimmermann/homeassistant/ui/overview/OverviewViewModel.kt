package com.hotmail.leon.zimmermann.homeassistant.ui.overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductRepository
import kotlinx.coroutines.launch

class OverviewViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productEntityList: LiveData<List<ProductEntity>>

    init {
        val productDao = HomeAssistantDatabase.getDatabase(application, viewModelScope).productDao()
        productRepository = ProductRepository(productDao)
        productEntityList = Transformations.map(productRepository.productEntityList) { list ->
            list.filter { it.discrepancy > 0 }
        }
    }
    
    fun update(productEntity: ProductEntity) {
        viewModelScope.launch {
            productRepository.update(productEntity)
        }
    }
}
