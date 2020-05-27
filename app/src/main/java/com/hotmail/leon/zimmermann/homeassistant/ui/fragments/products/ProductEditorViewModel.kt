package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.products

import android.app.Application
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.Measure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.Category
import kotlinx.coroutines.launch

class ProductEditorViewModel(application: Application) : AndroidViewModel(application) {

    val productList: List<Product> = emptyList()
    val categoryList: List<Category> = emptyList()
    val measureList: List<Measure> = emptyList()

    var nameInputValue = MutableLiveData("")
    var capacityInputValue = MutableLiveData("")
    var currentInputValue = MutableLiveData("")
    var minInputValue = MutableLiveData("")
    var maxInputValue = MutableLiveData("")

    var productId: Long? = null

    init {
        // TODO Init lists
    }

    // TODO Add Validation
    fun save(categoryText: String, measureText: String) {
        val name = nameInputValue.value!!
        val category = categoryList.first { it.name == categoryText }
        val capacity = capacityInputValue.value!!.toDouble()
        val measure = measureList.first { it.name == measureText }
        val quantity = currentInputValue.value!!.toDouble()
        val min = minInputValue.value!!.toInt()
        val max = maxInputValue.value!!.toInt()
        if (productId == null) saveNewProduct(name, category, capacity, measure, quantity, min, max)
        else updateExistingProduct(name, category, capacity, measure, quantity, min, max)
        clearInputs()
    }

    private fun saveNewProduct(
        name: String,
        category: Category,
        capacity: Double,
        measure: Measure,
        quantity: Double,
        min: Int,
        max: Int
    ) {
        viewModelScope.launch {
            // TODO Implement
        }
    }

    private fun updateExistingProduct(
        name: String,
        category: Category,
        capacity: Double,
        measure: Measure,
        quantity: Double,
        min: Int,
        max: Int
    ) {
        viewModelScope.launch {
            // TODO Implement
        }
    }

    private fun clearInputs() {
        nameInputValue.value = ""
        capacityInputValue.value = ""
        currentInputValue.value = ""
        minInputValue.value = ""
        maxInputValue.value = ""
    }
}
