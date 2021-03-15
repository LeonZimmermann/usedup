package de.usedup.android.components.consumption

import android.content.DialogInterface
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.datamodel.api.exceptions.InvalidInputException
import de.usedup.android.datamodel.api.objects.MeasureValue
import de.usedup.android.datamodel.api.repositories.MeasureRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConsumptionElementDialogViewModel @Inject constructor(
  private val productRepository: ProductRepository,
  private val measureRepository: MeasureRepository
) : ViewModel(), AdapterView.OnItemClickListener {

  private val measures = measureRepository.measures
  var productNames: MutableLiveData<List<String>> = MutableLiveData()
  val measureNames = MutableLiveData(measureRepository.measures.map { it.name })

  val nameText: MutableLiveData<String> = MutableLiveData()
  val consumptionText: MutableLiveData<String> = MutableLiveData()
  val measureText: MutableLiveData<String> = MutableLiveData()
  val measureInputType: MutableLiveData<Int> = MutableLiveData(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE)

  init {
    viewModelScope.launch(Dispatchers.IO) {
      productNames.postValue(productRepository.getAllProducts().map { it.name })
    }
  }

  fun onPositiveButtonClicked(callback: ConsumptionElementDialogFragment.Callback) {
    viewModelScope.launch {
      val product = nameText.value?.let { productRepository.getProductForName(it) } ?: throw InvalidInputException(
        "Invalid product name")
      val measure = measureText.value?.let { measureRepository.getMeasureForName(it) } ?: throw InvalidInputException(
        "Invalid measure")
      val consumption = consumptionText.value?.toDoubleOrNull() ?: throw InvalidInputException("Invalid consumption value")
      callback.onPositiveButtonClicked(ConsumptionElement(product, MeasureValue(consumption, measure)))
    }
  }

  override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    viewModelScope.launch {
      if (productNames.value != null) {
        val product = productRepository.getProductForName(requireNotNull(productNames.value)[position])
        if (product != null) {
          val productMeasure = measureRepository.getMeasureForId(product.measureId)
          if (productMeasure != null) {
            val measureList = measures.filter { it.type == productMeasure.type }.map { it.name }
            measureNames.postValue(measureList)
            measureText.postValue(if (measureList.size > 1) "" else productMeasure.name)
            measureInputType.postValue(if (measureList.size > 1) InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE else 0)
          }
        }
      }
    }
  }

  fun onNegativeButtonClicked(dialogInterface: DialogInterface) {
    dialogInterface.cancel()
  }
}