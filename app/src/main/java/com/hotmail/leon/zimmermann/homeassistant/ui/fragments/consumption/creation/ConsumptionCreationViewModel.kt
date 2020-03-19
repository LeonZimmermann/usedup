package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.creation

import android.app.Application
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionList
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionListMetaDataEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionRepository
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductRepository
import kotlinx.coroutines.launch

class ConsumptionCreationViewModel(application: Application) : AndroidViewModel(application) {
    var nameString = MutableLiveData<String>()
    var durationString = MutableLiveData<String>()
    var descriptionString = MutableLiveData<String>()
    var instructionsString = MutableLiveData<String>()
    val consumptionTemplateList: MutableLiveData<MutableList<ConsumptionTemplate>> by lazy {
        MutableLiveData<MutableList<ConsumptionTemplate>>().apply {
            value = mutableListOf()
        }
    }

    private val consumptionRepository: ConsumptionRepository
    val productList: LiveData<List<ProductEntity>>

    private val name: String?
        get() = nameString.value
    private val duration: Int?
        get() = durationString.value?.toInt()
    private val description: String?
        get() = descriptionString.value
    private val instructions: String?
        get() = instructionsString.value

    init {
        val database = HomeAssistantDatabase.getDatabase(application, viewModelScope)
        consumptionRepository = ConsumptionRepository(database.consumptionDao())
        val productRepository = ProductRepository(database.productDao())
        productList = productRepository.productEntityList
    }

    fun addConsumptionTemplate(consumptionTemplate: ConsumptionTemplate) {
        val consumptionTemplateList = consumptionTemplateList.value
        consumptionTemplateList?.add(consumptionTemplate)
        this.consumptionTemplateList.value = consumptionTemplateList
    }

    fun saveDinnerToDatabase() {
        // TODO Check if name is null and consumptionList empty
        viewModelScope.launch {
            consumptionRepository.insert(
                ConsumptionList(
                    ConsumptionListMetaDataEntity(name!!, duration, description, instructions),
                    consumptionTemplateList.value!!.toList().map { ConsumptionEntity(it.product.id, it.measure.id, it.value) }
                )
            )
        }
    }
}
