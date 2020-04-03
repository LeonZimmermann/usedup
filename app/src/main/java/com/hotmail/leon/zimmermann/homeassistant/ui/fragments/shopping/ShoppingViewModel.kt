package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import android.app.Application
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductRepository
import kotlinx.coroutines.launch

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productList: LiveData<List<ProductEntity>>

    private val categoryRepository: CategoryRepository
    val categoryList: LiveData<List<CategoryEntity>>

    val shoppingList: MutableLiveData<MutableMap<Long, MutableList<ShoppingProduct>>>

    init {
        val database = HomeAssistantDatabase.getDatabase(application, viewModelScope)
        val productDao = database.productDao()
        productRepository = ProductRepository(productDao)
        productList = productRepository.productEntityList
        val categoryDao = database.categoryDao()
        categoryRepository = CategoryRepository(categoryDao)
        categoryList = categoryRepository.categoryList
        shoppingList = Transformations.map(productRepository.productEntityList) { list ->
            list.asSequence()
                .filter { product -> product.discrepancy > 0 }
                .groupBy { it.categoryId }
                .toList()
                .map { Pair(it.first, it.second.map { product -> ShoppingProduct(product) }.toMutableList()) }
                .toMap()
                .toMutableMap()
        } as MutableLiveData<MutableMap<Long, MutableList<ShoppingProduct>>>
    }

    fun updateAll(productEntityList: List<ProductEntity>) {
        viewModelScope.launch {
            productRepository.updateAll(productEntityList)
        }
    }

}
