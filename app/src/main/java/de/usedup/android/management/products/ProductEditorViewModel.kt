package de.usedup.android.management.products

import android.view.View
import android.widget.AdapterView
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.datamodel.api.exceptions.InvalidInputException
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.repositories.CategoryRepository
import de.usedup.android.datamodel.api.repositories.MeasureRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProductEditorViewModel @Inject constructor(
  private val productRepository: ProductRepository,
  private val measureRepository: MeasureRepository,
  private val categoryRepository: CategoryRepository
) : ViewModel(), AdapterView.OnItemClickListener {

  val categoryList = categoryRepository.categories
  val measureList = measureRepository.measures

  val nameInputValue = MutableLiveData("")
  val categoryInputValue = MutableLiveData("")
  val capacityInputValue = MutableLiveData("")
  val capacityInputEnabled = MutableLiveData(false)
  val measureInputValue = MutableLiveData("")
  val currentInputValue = MutableLiveData("")
  val minInputValue = MutableLiveData("")
  val maxInputValue = MutableLiveData("")
  val systemMessage = MutableLiveData("")

  var productId: Id? = null
    private set

  fun setProductId(productId: Id) = viewModelScope.launch(Dispatchers.Default) {
    this@ProductEditorViewModel.productId = productId
    productRepository.getProductForId(productId)?.let { product ->
      nameInputValue.postValue(product.name)
      capacityInputValue.postValue(product.capacity.toString())
      val category = categoryRepository.getCategoryForId(product.categoryId)
      if (category != null) {
        categoryInputValue.postValue(category.name)
      }
      currentInputValue.postValue(product.quantity.toString())
      val measure = measureRepository.getMeasureForId(product.measureId)
      if (measure != null) {
        measureInputValue.postValue(measure.name)
      }
      minInputValue.postValue(product.min.toString())
      maxInputValue.postValue(product.max.toString())
    }
  }

  fun save(view: View) = viewModelScope.launch(Dispatchers.Default) {
    try {
      when {
        nameInputValue.value.isNullOrBlank() -> throw InvalidInputException("Insert a name")
        categoryInputValue.value.isNullOrBlank() -> throw InvalidInputException("Select a category")
        capacityInputValue.value.isNullOrBlank() -> throw InvalidInputException("Insert a capacity")
        measureInputValue.value.isNullOrBlank() -> throw InvalidInputException("Select a measure")
        currentInputValue.value.isNullOrBlank() -> throw InvalidInputException("Insert the current quantity")
        minInputValue.value.isNullOrBlank() -> throw InvalidInputException("Insert a minimum quantity")
        maxInputValue.value.isNullOrBlank() -> throw InvalidInputException("Insert a maximum quantity")
        else -> saveAfterValidation(view)
      }
    } catch (e: InvalidInputException) {
      systemMessage.value = e.message
    }
  }

  private fun saveAfterValidation(view: View) = viewModelScope.launch(Dispatchers.IO) {
    val name = requireNotNull(nameInputValue.value)
    val category =
      categoryRepository.getCategoryForName(requireNotNull(categoryInputValue.value)) ?: throw InvalidInputException(
        "Select a valid category")
    val capacity = requireNotNull(capacityInputValue.value).toDouble()
    val measure =
      measureRepository.getMeasureForName(requireNotNull(measureInputValue.value)) ?: throw InvalidInputException(
        "Select a valid measure")
    val quantity = requireNotNull(currentInputValue.value).toDouble()
    val min = requireNotNull(minInputValue.value).toInt()
    val max = requireNotNull(maxInputValue.value).toInt()
    try {
      if (productId == null) productRepository.addProduct(name, category.id, capacity, measure.id, quantity, min, max)
      else productRepository.updateProduct(requireNotNull(productId), name, category.id, capacity, measure.id, quantity,
        min, max)
      systemMessage.postValue("Saved Product")
      clearData()
      launch(Dispatchers.Main) {
        Navigation.findNavController(view).navigateUp()
      }
    } catch (e: IOException) {
      systemMessage.postValue("Could not save product")
    }
  }

  private fun clearData() {
    nameInputValue.postValue("")
    categoryInputValue.postValue("")
    capacityInputValue.postValue("")
    capacityInputEnabled.postValue(false)
    measureInputValue.postValue("")
    currentInputValue.postValue("")
    minInputValue.postValue("")
    maxInputValue.postValue("")
    systemMessage.postValue("")
    productId = null
  }

  override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
    val measureName = requireNotNull(parent.adapter.getItem(position) as? String)
    val measure = measureRepository.getMeasureForName(measureName)
    if (measure != null) {
      capacityInputEnabled.postValue(measure.complex)
    }
  }
}
