package com.hotmail.leon.zimmermann.homeassistant.ui.shopping

import android.app.Application
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductRepository
import kotlinx.coroutines.launch

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productEntityList: LiveData<List<ProductEntity>>

    init {
        val productDao = HomeAssistantDatabase.getDatabase(application, viewModelScope).productDao()
        productRepository = ProductRepository(productDao)
        productEntityList = Transformations.map(productRepository.productEntityList) { list ->
            list.filter { product -> product.discrepancy > 0 }
        }
    }

    fun updateAll(productEntityList: List<ProductEntity>) {
        viewModelScope.launch {
            productRepository.updateAll(productEntityList)
        }
    }

}
