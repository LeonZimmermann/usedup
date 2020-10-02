package com.hotmail.leon.zimmermann.homeassistant.app.management.products

import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.InvalidInputException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.CategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class ProductEditorViewModel : ViewModel() {
  val categoryList = CategoryRepository.categories
  val measureList = MeasureRepository.measures

  var nameInputValue = MutableLiveData("")
  var categoryInputValue = MutableLiveData("")
  var capacityInputValue = MutableLiveData("")
  var measureInputValue = MutableLiveData("")
  var currentInputValue = MutableLiveData("")
  var minInputValue = MutableLiveData("")
  var maxInputValue = MutableLiveData("")

  var systemMessage = MutableLiveData("")

  var productId: String? = null
    private set

  fun setProductId(productId: String) = viewModelScope.launch(Dispatchers.Default) {
    this@ProductEditorViewModel.productId = productId
    ProductRepository.getProductForId(productId).let { product ->
      nameInputValue.value = product.name
      capacityInputValue.value = product.capacity.toString()
      categoryInputValue.value = CategoryRepository.getCategoryForId(product.categoryId).name
      currentInputValue.value = product.quantity.toString()
      measureInputValue.value = MeasureRepository.getMeasureForId(product.measureId).name
      minInputValue.value = product.min.toString()
      maxInputValue.value = product.max.toString()
    }
  }

  fun save() = viewModelScope.launch(Dispatchers.Default) {
    try {
      when {
        nameInputValue.value.isNullOrBlank() -> throw InvalidInputException("Insert a name")
        categoryInputValue.value.isNullOrBlank() -> throw InvalidInputException("Select a category")
        capacityInputValue.value.isNullOrBlank() -> throw InvalidInputException("Insert a capacity")
        measureInputValue.value.isNullOrBlank() -> throw InvalidInputException("Select a measure")
        currentInputValue.value.isNullOrBlank() -> throw InvalidInputException("Insert the current quantity")
        minInputValue.value.isNullOrBlank() -> throw InvalidInputException("Insert a minimum quantity")
        maxInputValue.value.isNullOrBlank() -> throw InvalidInputException("Insert a maximum quantity")
        else -> saveAfterValidation()
      }
    } catch (e: InvalidInputException) {
      systemMessage.value = e.message
    }
  }

  private fun saveAfterValidation() = viewModelScope.launch(Dispatchers.Default) {
    val name = nameInputValue.value!!
    val category = CategoryRepository.getCategoryForName(categoryInputValue.value!!)
    val capacity = capacityInputValue.value!!.toDouble()
    val measure = MeasureRepository.getMeasureForName(measureInputValue.value!!)
    val quantity = currentInputValue.value!!.toDouble()
    val min = minInputValue.value!!.toInt()
    val max = maxInputValue.value!!.toInt()
    try {
      if (productId == null) ProductRepository.addProduct(name, category.id, capacity, measure.id, quantity, min,
          max)
      else ProductRepository.updateProduct(productId!!, name, category.id, capacity, measure.id, quantity, min, max)
      systemMessage.value = "Added Product"
      // TODO Navigate up
    } catch (e: IOException) {
      systemMessage.value = "Could not add product"
    }
  }
}
