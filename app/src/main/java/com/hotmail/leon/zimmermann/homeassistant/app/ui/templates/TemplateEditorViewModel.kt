package com.hotmail.leon.zimmermann.homeassistant.app.ui.templates

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseTemplate
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.TemplateRepository
import kotlinx.coroutines.launch

class TemplateEditorViewModel : ViewModel() {
    private val database = Firebase.firestore
    var products: List<Product> = ProductRepository.products
    val measures: MutableList<Measure> = MeasureRepository.measures

    var nameString = MutableLiveData<String>()
    val consumptionElementList: MutableLiveData<MutableList<com.hotmail.leon.zimmermann.homeassistant.components.consumption.ConsumptionElement>> by lazy {
        MutableLiveData<MutableList<com.hotmail.leon.zimmermann.homeassistant.components.consumption.ConsumptionElement>>().apply {
            value = mutableListOf()
        }
    }

    private val name: String?
        get() = nameString.value

    var templateId: String? = null
        private set

    fun setTemplateId(templateId: String) {
        this.templateId = templateId
        database.collection(FirebaseTemplate.COLLECTION_NAME)
            .document(templateId)
            .get()
            .addOnSuccessListener { result ->
                result.toObject<Template>()?.apply {
                    nameString.value = name
                    components.let {
                        consumptionElementList.value = it.map { element ->
                            val product = ProductRepository.getProductForId(element.productId)
                            val measure = MeasureRepository.getMeasureForId(element.measureId)
                            com.hotmail.leon.zimmermann.homeassistant.components.consumption.ConsumptionElement(
                                product,
                                ValueWithMeasure(element.value, measure)
                            )
                        }.toMutableList()
                    }
                } ?: throw RuntimeException("Couldn't convert template") // TODO Should not be a RuntimeException
            }
    }

    fun addConsumptionElement(consumptionElement: com.hotmail.leon.zimmermann.homeassistant.components.consumption.ConsumptionElement) {
        val consumptionElementList = consumptionElementList.value
        consumptionElementList?.let { list ->
            list.firstOrNull { it.product == consumptionElement.product }?.apply {
                this.value += consumptionElement.value
            } ?: list.add(consumptionElement)
        }
        this.consumptionElementList.value = consumptionElementList
    }

    fun saveTemplateToDatabase() {
        // TODO Check if name is null and consumptionList empty (Validation)
        viewModelScope.launch {
            TemplateRepository.addTemplate(name!!, consumptionElementList.value!!.map { element ->
                TemplateComponent(
                    element.product.id,
                    element.value.measure.id,
                    element.value.double
                )
            })
        }
    }
}