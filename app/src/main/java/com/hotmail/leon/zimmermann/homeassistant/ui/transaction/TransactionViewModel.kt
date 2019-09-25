package com.hotmail.leon.zimmermann.homeassistant.fragments.transaction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.models.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.product.ProductRepository
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository
    val productEntityList: LiveData<List<ProductEntity>>
    var transactionList: MutableLiveData<MutableList<Pair<ProductEntity, Int>>>

    init {
        val productDao = HomeAssistantDatabase.getDatabase(application, viewModelScope).productDao()
        repository = ProductRepository(productDao)
        productEntityList = repository.productList
        transactionList = MutableLiveData(mutableListOf())
    }

    fun updateAll(productEntityList: List<ProductEntity>) {
        viewModelScope.launch {
            repository.updateAll(productEntityList)
        }
    }

}
