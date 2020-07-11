package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import android.app.Application
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.CategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {
    val productDiscrepancies = ProductRepository.products
        .filter { it.discrepancy > 0 }
        .map { ShoppingProduct(it) }
        .groupBy { CategoryRepository.getCategoryForId(it.product.category.id) }
        .toList()
}
