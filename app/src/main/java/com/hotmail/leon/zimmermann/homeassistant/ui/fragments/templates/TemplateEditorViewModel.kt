package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.templates

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import com.hotmail.leon.zimmermann.homeassistant.ui.components.consumption.ConsumptionElement
import kotlinx.coroutines.launch

class TemplateEditorViewModel : ViewModel() {
    private val database = Firebase.firestore
    var products: List<Pair<String, Product>> = ProductRepository.products
    val measures: MutableList<Pair<String, Measure>> = MeasureRepository.measures

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

    fun setTemplateId(templateId: String) {
        this.templateId = templateId
        database.collection(Template.COLLECTION_NAME).document(templateId).get().addOnSuccessListener { result ->
            result.toObject<Template>()?.apply {
                nameString.value = name
                components?.let {
                    consumptionElementList.value = it.map { element ->
                        val product = products.first { it.first == element.product!!.id }.second
                        val measure = MeasureRepository.getMeasureForId(element.measure!!.id)
                        ConsumptionElement(
                            product,
                            Value(element.value!!, measure)
                        )
                    }.toMutableList()
                }
            } ?: throw RuntimeException("Couln't convert template") // TODO Should not be a RuntimeException
        }
    }


    fun addConsumptionElement(consumptionElement: ConsumptionElement) {
        val consumptionElementList = consumptionElementList.value
        consumptionElementList?.let { list ->
            list.firstOrNull { it.product == consumptionElement.product }?.apply {
                this.value += consumptionElement.value
            }
                ?: list.add(consumptionElement)
        }
        this.consumptionElementList.value = consumptionElementList
    }

    fun saveTemplateToDatabase() {
        // TODO Check if name is null and consumptionList empty (Validation)
        viewModelScope.launch {
            database.collection(Template.COLLECTION_NAME)
                .add(
                    Template(
                        name!!,
                        consumptionElementList.value!!.map { element ->
                            TemplateComponent(
                                database.collection(Product.COLLECTION_NAME)
                                    .document(products.first { it.second.name == element.product.name }.first),
                                database.collection(Measure.COLLECTION_NAME)
                                    .document(measures.first { it.second.name == element.value.measure.name }.first),
                                element.value.double
                            )
                        })
                )
        }
    }
}