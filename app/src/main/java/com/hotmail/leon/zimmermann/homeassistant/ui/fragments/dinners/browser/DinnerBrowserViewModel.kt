package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.dinners.browser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionList
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionRepository

class DinnerBrowserViewModel(application: Application) : AndroidViewModel(application) {

    private val consumptionRepository: ConsumptionRepository
    val consumptionLists: LiveData<List<ConsumptionList>>

    init {
        val database = HomeAssistantDatabase.getDatabase(application, viewModelScope)
        consumptionRepository = ConsumptionRepository(database.consumptionDao())
        consumptionLists = consumptionRepository.consumptionLists
    }
}
