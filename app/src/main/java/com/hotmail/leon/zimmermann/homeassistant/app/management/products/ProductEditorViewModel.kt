package com.hotmail.leon.zimmermann.homeassistant.app.management.products

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.exceptions.InvalidInputException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.CategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductEditorViewModel @ViewModelInject constructor(
  private val productRepository: ProductRepository,
  private val measureRepository: MeasureRepository,
  private val categoryRepository: CategoryRepository
) : ViewModel() {


  val categoryList = categoryRepository.categories
  val measureList = measureRepository.measures

  var nameInputValue = MutableLiveData("")
  var categoryInputValue = MutableLiveData("")
  var capacityInputValue = MutableLiveData("")
  var measureInputValue = MutableLiveData("")
  var currentInputValue = MutableLiveData("")
  var minInputValue = MutableLiveData("")
  var maxInputValue = MutableLiveData("")

  var systemMessage = MutableLiveData("")

  var productId: Id? = null
    private set

  fun setProductId(productId: Id) = viewModelScope.launch(Dispatchers.Default) {
    this@ProductEditorViewModel.productId = productId
    productRepository.getProductForId(productId).let { product ->
      nameInputValue.postValue(product.name)
      capacityInputValue.postValue(product.capacity.toString())
      categoryInputValue.postValue(categoryRepository.getCategoryForId(product.categoryId).name)
      currentInputValue.postValue(product.quantity.toString())
      measureInputValue.postValue(measureRepository.getMeasureForId(product.measureId).name)
      minInputValue.postValue(product.min.toString())
      maxInputValue.postValue(product.max.toString())
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
    TODO("Implement")
    /*
    val name = nameInputValue.value!!
    val category = FirebaseCategoryRepository.getCategoryForName(categoryInputValue.value!!)
    val capacity = capacityInputValue.value!!.toDouble()
    val measure = FirebaseMeasureRepository.getMeasureForName(measureInputValue.value!!)
    val quantity = currentInputValue.value!!.toDouble()
    val min = minInputValue.value!!.toInt()
    val max = maxInputValue.value!!.toInt()
    try {
      if (productId == null) FirebaseProductRepository.addProduct(name, category.id, capacity, measure.id, quantity,
        min,
        max)
      else FirebaseProductRepository.updateProduct(productId!!, name, category.id, capacity, measure.id, quantity, min,
        max)
      systemMessage.value = "Added Product"
      // TODO Navigate up
    } catch (e: IOException) {
      systemMessage.value = "Could not add product"
    }
     */
  }
}
