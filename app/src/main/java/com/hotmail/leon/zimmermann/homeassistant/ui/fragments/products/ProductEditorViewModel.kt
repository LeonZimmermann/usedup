package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.products

import androidx.lifecycle.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.callbacks.FirestoreCallback
import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.InvalidInputException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.CategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository
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
                initCategory(CategoryRepository.getCategoryForId(product.category.id))
                currentInputValue.value = product.quantity.toString()
                initMeasure(MeasureRepository.getMeasureForId(product.measure.id))
                minInputValue.value = product.min.toString()
                maxInputValue.value = product.max.toString()
            }
        }
    }

    fun save(categoryText: String, measureText: String, callback: FirestoreCallback) {
        viewModelScope.launch {
            when {
                nameInputValue.value.isNullOrBlank() -> callback.onValidationFailed(InvalidInputException("Insert a name"))
                categoryText.isBlank() -> callback.onValidationFailed(InvalidInputException("Select a category"))
                capacityInputValue.value.isNullOrBlank() -> callback.onValidationFailed(InvalidInputException("Insert a capacity"))
                measureText.isBlank() -> callback.onValidationFailed(InvalidInputException("Select a measure"))
                currentInputValue.value.isNullOrBlank() -> callback.onValidationFailed(InvalidInputException("Insert the current quantity"))
                minInputValue.value.isNullOrBlank() -> callback.onValidationFailed(InvalidInputException("Insert a minimum quantity"))
                maxInputValue.value.isNullOrBlank() -> callback.onValidationFailed(InvalidInputException("Insert a maximum quantity"))
                else -> saveAfterValidation(categoryText, measureText, callback)
            }
        }
    }

    private fun saveAfterValidation(categoryText: String, measureText: String, callback: FirestoreCallback) {
        val name = nameInputValue.value!!
        val category = CategoryRepository.getCategoryForName(categoryText)
        val capacity = capacityInputValue.value!!.toDouble()
        val measure = MeasureRepository.getMeasureForName(measureText)
        val quantity = currentInputValue.value!!.toDouble()
        val min = minInputValue.value!!.toInt()
        val max = maxInputValue.value!!.toInt()
        callback.onFirestoreResult(
            if (productId == null) ProductRepository.addProduct(name, category, capacity, measure, quantity, min, max)
            else ProductRepository.updateProduct(productId!!, name, category, capacity, measure, quantity, min, max)
        )
    }
}
