package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.products

import android.app.Application
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductRepository
import kotlinx.coroutines.launch

class ProductEditorViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productList: LiveData<List<ProductEntity>>

    val categoryList: LiveData<List<CategoryEntity>>
    val measureList: LiveData<List<MeasureEntity>>

    var nameInputValue = MutableLiveData("")
    var capacityInputValue = MutableLiveData("")
    var currentInputValue = MutableLiveData("")
    var minInputValue = MutableLiveData("")
    var maxInputValue = MutableLiveData("")

    var productId: Long? = null

    init {
        val database = HomeAssistantDatabase.getDatabase(application, viewModelScope)
        productRepository = ProductRepository(database.productDao())
        productList = productRepository.productEntityList
        val categoryRepository = CategoryRepository(database.categoryDao())
        categoryList = categoryRepository.categoryList
        val measureRepository = MeasureRepository(database.measureDao())
        measureList = measureRepository.measureEntityList
    }

    // TODO Add Validation
    fun save(categoryText: String, measureText: String) {
        val name = nameInputValue.value!!
        val category = categoryList.value!!.first { it.name == categoryText }
        val capacity = capacityInputValue.value!!.toDouble()
        val measure = measureList.value!!.first { it.text == measureText }
        val quantity = currentInputValue.value!!.toDouble()
        val min = minInputValue.value!!.toInt()
        val max = maxInputValue.value!!.toInt()
        if (productId == null) saveNewProduct(name, category, capacity, measure, quantity, min, max)
        else updateExistingProduct(name, category, capacity, measure, quantity, min, max)
        clearInputs()
    }

    private fun saveNewProduct(
        name: String,
        category: CategoryEntity,
        capacity: Double,
        measure: MeasureEntity,
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
        category: CategoryEntity,
        capacity: Double,
        measure: MeasureEntity,
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
