package com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption

class ConsumptionRepository(private val consumptionDao: ConsumptionDao) {

    val consumptionLists: List<ConsumptionList> = consumptionDao.getAll()

    suspend fun insert(consumptionList: ConsumptionList) {
        consumptionDao.insert(consumptionList.consumptions)
    }
}