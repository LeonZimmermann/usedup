package com.hotmail.leon.zimmermann.homeassistant.fragments.transaction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.models.product.Product
import com.hotmail.leon.zimmermann.homeassistant.models.product.ProductDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.product.ProductRepository
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository
    val productList: LiveData<List<Product>>
    var transactionList: MutableLiveData<MutableList<Pair<Product, Int>>>

    init {
        val productDao = ProductDatabase.getDatabase(application, viewModelScope).productDao()
        repository = ProductRepository(productDao)
        productList = repository.productList
        transactionList = MutableLiveData(mutableListOf())
    }

    fun updateAll(productList: List<Product>) {
        viewModelScope.launch {
            repository.updateAll(productList)
        }
    }

}
