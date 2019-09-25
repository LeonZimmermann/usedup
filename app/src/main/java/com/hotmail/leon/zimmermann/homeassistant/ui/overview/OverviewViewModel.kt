package com.hotmail.leon.zimmermann.homeassistant.fragments.overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.models.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.product.ProductRepository

class OverviewViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productEntityList: LiveData<List<ProductEntity>>

    init {
        val productDao = HomeAssistantDatabase.getDatabase(application, viewModelScope).productDao()
        productRepository = ProductRepository(productDao)
        productEntityList = Transformations.map(productRepository.productList) { list ->
            list.filter { it.discrepancy > 0 }
        }
    }
}
