package com.hotmail.leon.zimmermann.homeassistant.stockManagement

import androidx.lifecycle.ViewModel
import com.hotmail.leon.zimmermann.homeassistant.product.Product
import com.hotmail.leon.zimmermann.homeassistant.utils.MutableListLiveData

class StockManagementViewModel : ViewModel() {
    val productList: MutableListLiveData<Product> by lazy {
        MutableListLiveData(mutableListOf(
            Product("Toast", 0, 0, 0),
            Product("Salami", 0, 0, 0),
            Product("Cheese", 0, 0, 0),
            Product("Jam", 0, 0, 0)
        ))
    }
}
