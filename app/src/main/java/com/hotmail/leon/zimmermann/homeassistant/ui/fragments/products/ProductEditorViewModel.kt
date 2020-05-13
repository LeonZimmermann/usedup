package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.products

import android.app.Application
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.Product
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductRepository
import kotlinx.coroutines.launch

class ProductEditorViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productList: LiveData<List<Product>>

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
        productList = productRepository.getProductList(
            database.categoryDao().getAllStatically(),
            database.measureDao().getAllStatically()
        )
        val categoryRepository = CategoryRepository(database.categoryDao())
        categoryList = categoryRepository.categoryList
        val measureRepository = MeasureRepository(database.measureDao())
        measureList = measureRepository.measureList
    }

    // TODO Add Validation
    fun save(categoryText: String, measureText: String) {
        val name = nameInputValue.value!!
        val category = categoryList.value!!.first { it.text == categoryText }
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
            productRepository.insert(Product(name, category, capacity, measure, quantity, min, max))
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
            productRepository.getProduct(productId!!, categoryList.value!!, measureList.value!!).let {
                it.name = name
                it.category = category
                it.capacity = capacity
                it.measure = measure
                it.quantity = quantity
                it.min = min
                it.max = max
                productRepository.update(it)
            }
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
