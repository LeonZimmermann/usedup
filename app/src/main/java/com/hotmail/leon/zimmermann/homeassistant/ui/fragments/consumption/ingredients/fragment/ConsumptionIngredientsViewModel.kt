package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.ingredients.fragment

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

class ConsumptionIngredientsViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    val productEntityList: LiveData<List<ProductEntity>>

    private val consumptionRepository: ConsumptionRepository
    private var consumptionLists: LiveData<List<ConsumptionList>>

    var consumptionList: MutableLiveData<MutableList<ConsumptionIngredientsTemplate>> = MutableLiveData(mutableListOf())
    val consumptionListEmpty: Boolean
        get() = consumptionList.value.isNullOrEmpty()
    val consumptionListString: String
        get() = consumptionList.value?.joinToString("\n") {
            "- ${it.value}${it.measure.abbreviation} ${it.product.name}"
        } ?: ""

    init {
        val database = HomeAssistantDatabase.getDatabase(application, viewModelScope)

        val productDao = database.productDao()
        productRepository = ProductRepository(productDao)
        productEntityList = productRepository.productEntityList

        val consumptionDao = database.consumptionDao()
        consumptionRepository = ConsumptionRepository(consumptionDao)
        consumptionLists = consumptionRepository.consumptionLists
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

    // TODO Use this code for advanced consumption
    /*
    fun consume() {
            val consumptionsMade = mutableListOf<ConsumptionIngredientsTemplate>()
            try {
                val consumptionList = consumptionList.value!!
                if (consumptionList.isEmpty()) throw NoConsumptionsException()
                for (consumption in consumptionList) {
                    consumption.product.reduce(consumption.value, consumption.measure)
                    consumptionsMade.add(consumption)
                }
                updateAll(consumptionList.map { it.product })
                this.consumptionList.value = mutableListOf()
            } catch (e: ConsumptionIngredientsException) {
                catchConsumptionException(consumptionsMade, e)
            } catch (e: ProductEntity.ProductReductionException) {
                catchConsumptionException(consumptionsMade, e)
            }
    }

    private fun catchConsumptionException(consumptionsMade: List<ConsumptionIngredientsTemplate>, exception: Exception) {
        consumptionsMade.forEach { it.product.reduce(-it.value, it.measure) }
        updateAll(consumptionsMade.map { it.product })
        //Toast.makeText(context!!, exception.message, Toast.LENGTH_LONG).show()
    }
     */
}
