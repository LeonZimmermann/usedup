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
    private val productList: LiveData<List<ProductEntity>>

    private val categoryRepository: CategoryRepository
    val categoryList: LiveData<List<CategoryEntity>>

    val shoppingList: MutableLiveData<Map<Int, List<ShoppingProduct>>>
    var editShoppingEntryIndex: Int? = null

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
                .map { Pair(it.first, it.second.map { product -> ShoppingProduct(product) }) }
                .toMap()
        } as MutableLiveData<Map<Int, List<ShoppingProduct>>>
    }

    /*
    fun addShoppingEntry(category: ShoppingCategory) {
        performAndNotifyOnLiveData(shoppingList) { shoppingList -> shoppingList!!.add(category) }
    }
    */

    /*
    fun editShoppingEntry(index: Int, product: ProductEntity, amount: Int) {
        performAndNotifyOnLiveData(shoppingList) { shoppingList ->
            val shoppingEntry = shoppingList!![index]
            shoppingEntry.product = product
            shoppingEntry.amount = amount
        }
    }
    */

    /*
    fun removeShoppingEntryAt(index: Int) {
        performAndNotifyOnLiveData(shoppingList) { shoppingList -> shoppingList!!.removeAt(index) }
    }
    */

    private fun <T> performAndNotifyOnLiveData(obj: MutableLiveData<T>, function: (T?) -> Unit) {
        val liveDataValue = obj.value
        function(liveDataValue)
        obj.value = liveDataValue
    }

    fun updateAll(productEntityList: List<ProductEntity>) {
        viewModelScope.launch {
            productRepository.updateAll(productEntityList)
        }
    }

}
