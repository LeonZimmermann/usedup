package com.hotmail.leon.zimmermann.homeassistant.components.consumption

import android.content.DialogInterface
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.exceptions.InvalidInputException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.MeasureValue
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import kotlinx.coroutines.launch

class ConsumptionElementDialogViewModel @ViewModelInject constructor(
  private val productRepository: ProductRepository,
  private val measureRepository: MeasureRepository
) : ViewModel() {

  val productNames = Transformations.map(productRepository.products) { products -> products.map { it.name } }
  val measureNames = MutableLiveData(measureRepository.measures.map { it.name })

  val nameText: MutableLiveData<String> = MutableLiveData()
  val consumptionText: MutableLiveData<String> = MutableLiveData()
  val measureText: MutableLiveData<String> = MutableLiveData()

  var callback: ((consumptionElement: ConsumptionElement) -> Unit)? = null

  fun onPositiveButtonClicked() {
    viewModelScope.launch {
      val product = nameText.value?.let { productRepository.getProductForName(it) } ?: throw InvalidInputException(
        "Invalid product name")
      val measure = measureText.value?.let { measureRepository.getMeasureForName(it) } ?: throw InvalidInputException(
        "Invalid measure")
      val consumption = consumptionText.value?.toDouble() ?: throw InvalidInputException("Invalid consumption value")
      requireNotNull(callback)(ConsumptionElement(product, MeasureValue(consumption, measure)))
    }
  }

  fun onNegativeButtonClicked(dialogInterface: DialogInterface) {
    dialogInterface.cancel()
  }
}