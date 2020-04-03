package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.settings

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.Category
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val categoryRepository: CategoryRepository =
        CategoryRepository(HomeAssistantDatabase.getDatabase(application, viewModelScope).categoryDao())
    var categoryOrder: List<Category> = Category.values().toList()
        private set

    init {
        viewModelScope.launch {
            categoryOrder = categoryRepository.getAllStatically()
                .sortedBy { it.position }
                .map { Category.values()[it.id.toInt()] }
        }
    }

    fun setCategoryOrder(categoryOrder: List<Category>) {
        this.categoryOrder = categoryOrder
        viewModelScope.launch {
            val categoryList = categoryRepository.getAllStatically()
            categoryOrder.forEachIndexed { index, category ->
                categoryList.first { it.id == category.id }.position = index
            }
            categoryRepository.update(categoryList)
        }
    }
}