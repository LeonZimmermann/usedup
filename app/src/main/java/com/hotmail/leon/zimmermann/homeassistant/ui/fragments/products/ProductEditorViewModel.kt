package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.products

import android.widget.TextView
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
    var categoryInputValue = MutableLiveData("")
    var capacityInputValue = MutableLiveData("")
    var measureInputValue = MutableLiveData("")
    var currentInputValue = MutableLiveData("")
    var minInputValue = MutableLiveData("")
    var maxInputValue = MutableLiveData("")

    var productId: String? = null
        private set

    fun setProductId(productId: String) {
        this.productId = productId
        database.collection(Product.COLLECTION_NAME).document(productId).get().addOnSuccessListener { document ->
            document.toObject<Product>()?.let { product ->
                nameInputValue.value = product.name
                capacityInputValue.value = product.capacity.toString()
                categoryInputValue.value = CategoryRepository.getCategoryForId(product.category.id).name
                currentInputValue.value = product.quantity.toString()
                measureInputValue.value = MeasureRepository.getMeasureForId(product.measure.id).name
                minInputValue.value = product.min.toString()
                maxInputValue.value = product.max.toString()
            }
        }
    }

    fun save(callback: FirestoreCallback) {
        viewModelScope.launch {
            when {
                nameInputValue.value.isNullOrBlank() -> callback.onValidationFailed(InvalidInputException("Insert a name"))
                categoryInputValue.value.isNullOrBlank() -> callback.onValidationFailed(InvalidInputException("Select a category"))
                capacityInputValue.value.isNullOrBlank() -> callback.onValidationFailed(InvalidInputException("Insert a capacity"))
                measureInputValue.value.isNullOrBlank() -> callback.onValidationFailed(InvalidInputException("Select a measure"))
                currentInputValue.value.isNullOrBlank() -> callback.onValidationFailed(InvalidInputException("Insert the current quantity"))
                minInputValue.value.isNullOrBlank() -> callback.onValidationFailed(InvalidInputException("Insert a minimum quantity"))
                maxInputValue.value.isNullOrBlank() -> callback.onValidationFailed(InvalidInputException("Insert a maximum quantity"))
                else -> saveAfterValidation(callback)
            }
        }
    }

    private fun saveAfterValidation(callback: FirestoreCallback) {
        val name = nameInputValue.value!!
        val category = CategoryRepository.getCategoryForName(categoryInputValue.value!!)
        val capacity = capacityInputValue.value!!.toDouble()
        val measure = MeasureRepository.getMeasureForName(measureInputValue.value!!)
        val quantity = currentInputValue.value!!.toDouble()
        val min = minInputValue.value!!.toInt()
        val max = maxInputValue.value!!.toInt()
        callback.onFirestoreResult(
            if (productId == null) ProductRepository.addProduct(name, category, capacity, measure, quantity, min, max)
            else ProductRepository.updateProduct(productId!!, name, category, capacity, measure, quantity, min, max)
        )
    }
}
