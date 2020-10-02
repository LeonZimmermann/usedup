package com.hotmail.leon.zimmermann.homeassistant.app.ui.templates

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.components.consumption.ConsumptionElement
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseTemplate
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.TemplateRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TemplateEditorViewModel : ViewModel() {
  private val database = Firebase.firestore
  var products: MutableLiveData<MutableList<Product>> = ProductRepository.products
  val measures: MutableList<Measure> = MeasureRepository.measures

  var nameString = MutableLiveData<String>()
  val consumptionElementList: MutableLiveData<MutableList<ConsumptionElement>> by lazy {
    MutableLiveData<MutableList<ConsumptionElement>>().apply {
      value = mutableListOf()
    }
  }

  private val name: String?
    get() = nameString.value

  var templateId: String? = null
    private set

  fun setTemplateId(templateId: String) = viewModelScope.launch(Dispatchers.IO) {
    this@TemplateEditorViewModel.templateId = templateId
    val task = database.collection(FirebaseTemplate.COLLECTION_NAME).document(templateId).get()
    Tasks.await(task)
    if (task.exception != null) throw task.exception!!
    else {
      task.result!!.toObject<Template>()?.apply {
        nameString.value = name
        components.let {
          consumptionElementList.value = it.map { element ->
            val product = ProductRepository.getProductForId(element.productId)
            val measure = MeasureRepository.getMeasureForId(element.measureId)
            ConsumptionElement(product, MeasureValue(element.value, measure))
          }.toMutableList()
        }
      } ?: throw RuntimeException("Couldn't convert template") // TODO Should not be a RuntimeException
    }
  }

  fun addConsumptionElement(consumptionElement: ConsumptionElement) {
    val consumptionElementList = consumptionElementList.value
    consumptionElementList?.let { list ->
      list.firstOrNull { it.product == consumptionElement.product }?.apply {
        this.valueValue += consumptionElement.valueValue
      } ?: list.add(consumptionElement)
    }
    this.consumptionElementList.value = consumptionElementList
  }

  fun saveTemplateToDatabase() = viewModelScope.launch(Dispatchers.IO) {
    // TODO Check if name is null and consumptionList empty (Validation)
    TemplateRepository.addTemplate(name!!, consumptionElementList.value!!.map { element ->
      TemplateComponent(
        element.product.id,
        element.valueValue.measure.id,
        element.valueValue.double
      )
    })
  }
}