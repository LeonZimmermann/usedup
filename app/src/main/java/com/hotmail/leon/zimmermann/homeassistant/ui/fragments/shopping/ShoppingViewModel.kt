package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import android.app.Application
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductRepository
import kotlinx.coroutines.launch

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productList: LiveData<List<ProductEntity>>

    val shoppingList: MutableLiveData<MutableList<ShoppingEntry>>
    var editShoppingEntryIndex: Int? = null

    init {
        val productDao = HomeAssistantDatabase.getDatabase(application, viewModelScope).productDao()
        productRepository = ProductRepository(productDao)
        productList = productRepository.productEntityList
        shoppingList = Transformations.map(productRepository.productEntityList) { list ->
            list.filter { product -> product.discrepancy > 0 }
                .map { product -> ShoppingEntry(product, product.discrepancy) }
                .toMutableList()
        } as MutableLiveData<MutableList<ShoppingEntry>>
    }

    fun addShoppingEntry(entry: ShoppingEntry) {
        performAndNotifyOnLiveData(shoppingList) { shoppingList -> shoppingList!!.add(entry) }
    }

    fun editShoppingEntry(index: Int, product: ProductEntity, amount: Int) {
        performAndNotifyOnLiveData(shoppingList) { shoppingList ->
            val shoppingEntry = shoppingList!![index]
            shoppingEntry.product = product
            shoppingEntry.amount = amount
        }
    }

    fun removeShoppingEntryAt(index: Int) {
        performAndNotifyOnLiveData(shoppingList) { shoppingList -> shoppingList!!.removeAt(index) }
    }

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
