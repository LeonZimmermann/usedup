package com.hotmail.leon.zimmermann.homeassistant.components.consumption

import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.InvalidInputException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Measure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.MeasureValue
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product.ProductRepository
import kotlinx.android.synthetic.main.consumption_element_dialog_fragment.view.*
import kotlinx.coroutines.launch

class ConsumptionElementDialogViewModel : ViewModel() {
  val productNames = Transformations.map(ProductRepository.products) { products -> products.map { it.name } }
  val measureNames = MutableLiveData(MeasureRepository.measures.map { it.name })

  val nameText: MutableLiveData<String> = MutableLiveData()
  val consumptionText: MutableLiveData<String> = MutableLiveData()
  val measureText: MutableLiveData<String> = MutableLiveData()

  lateinit var callback: (consumptionElement: ConsumptionElement) -> Unit

  fun onPositiveButtonClicked() {
    viewModelScope.launch {
      val product = nameText.value?.let { ProductRepository.getProductForName(it) } ?: throw InvalidInputException(
        "Invalid product name")
      val measure = measureText.value?.let { MeasureRepository.getMeasureForName(it) } ?: throw InvalidInputException(
        "Invalid measure")
      val consumption = consumptionText.value?.toDouble() ?: throw InvalidInputException("Invalid consumption value")
      callback(ConsumptionElement(product, MeasureValue(consumption, measure)))
    }
  }

  fun onNegativeButtonClicked(dialogInterface: DialogInterface) {
    dialogInterface.cancel()
  }
}