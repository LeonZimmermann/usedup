package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductRepository

class ConsumptionDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productList: LiveData<List<ProductEntity>>

    init {
        val database = HomeAssistantDatabase.getDatabase(application, viewModelScope)
        productRepository = ProductRepository(database.productDao())
        productList = productRepository.productEntityList
    }
}
