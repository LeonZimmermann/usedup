package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.products

import androidx.lifecycle.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.*
import kotlinx.coroutines.launch

class ProductEditorViewModel : ViewModel() {
    private val database = Firebase.firestore

    val categoryList = CategoryRepository.categories
    val measureList = MeasureRepository.measures

    var nameInputValue = MutableLiveData("")
    var capacityInputValue = MutableLiveData("")
    var currentInputValue = MutableLiveData("")
    var minInputValue = MutableLiveData("")
    var maxInputValue = MutableLiveData("")

    var productId: String? = null
        private set
    fun setProductId(productId: String, initCategory: (Category) -> Unit, initMeasure: (Measure) -> Unit) {
        this.productId = productId
        database.collection(Product.COLLECTION_NAME).document(productId).get().addOnSuccessListener { document ->
            document.toObject<Product>()?.let { product ->
                nameInputValue.value = product.name
                capacityInputValue.value = product.capacity.toString()
                product.category?.let { initCategory(CategoryRepository.getCategoryForId(it.id)) }
                currentInputValue.value = product.quantity.toString()
                product.measure?.let { initMeasure(MeasureRepository.getMeasureForId(it.id)) }
                minInputValue.value = product.min.toString()
                maxInputValue.value = product.max.toString()
            }
        }
    }

    // TODO Add Validation
    fun save(categoryText: String, measureText: String) {
        val name = nameInputValue.value!!
        val category = CategoryRepository.getCategoryForId(categoryText)
        val capacity = capacityInputValue.value!!.toDouble()
        val measure = MeasureRepository.getMeasureForId(measureText)
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
            database.collection(Product.COLLECTION_NAME).add(
                Product(
                    name,
                    quantity,
                    min,
                    max,
                    capacity,
                    database.collection(Measure.COLLECTION_NAME).document(MeasureRepository.getId(measure.name)),
                    database.collection(Category.COLLECTION_NAME).document(CategoryRepository.getId(category.name))
                )
            )
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
            database.collection(Product.COLLECTION_NAME).document(productId!!).update(
                mapOf(
                    "name" to name,
                    "category" to database.collection(Category.COLLECTION_NAME)
                        .document(CategoryRepository.getId(category.name)),
                    "capacity" to capacity,
                    "measure" to database.collection(Measure.COLLECTION_NAME)
                        .document(MeasureRepository.getId(measure.name)),
                    "quantity" to quantity,
                    "min" to min,
                    "max" to max
                )
            )
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
