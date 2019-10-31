package com.hotmail.leon.zimmermann.homeassistant.ui.consumption.ingredients

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionList
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionListMetaDataEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionRepository
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductRepository
import kotlinx.coroutines.launch

class IngredientsViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productEntityList: LiveData<List<ProductEntity>>

    private val consumptionRepository: ConsumptionRepository
    var consumptionLists: LiveData<List<ConsumptionList>>
    var consumptionList: MutableLiveData<MutableList<IngredientsTemplate>>

    init {
        val database = HomeAssistantDatabase.getDatabase(application, viewModelScope)

        val productDao = database.productDao()
        productRepository = ProductRepository(productDao)
        productEntityList = productRepository.productEntityList

        val consumptionDao = database.consumptionDao()
        consumptionRepository = ConsumptionRepository(consumptionDao)
        consumptionLists = consumptionRepository.consumptionLists
        consumptionList = MutableLiveData(mutableListOf())
    }

    fun updateAll(productEntityList: List<ProductEntity>) {
        viewModelScope.launch {
            productRepository.updateAll(productEntityList)
        }
    }

    fun save(name: String) {
        viewModelScope.launch {
            consumptionRepository.insert(
                ConsumptionList(
                    ConsumptionListMetaDataEntity(name),
                    consumptionList.value!!.map { ConsumptionEntity(it.product.id, it.measure.id, it.value) })
            )
        }
    }
}
