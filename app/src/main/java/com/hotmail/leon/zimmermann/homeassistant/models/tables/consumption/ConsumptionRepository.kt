package com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption

class ConsumptionRepository(private val consumptionDao: ConsumptionDao) {
    val consumptionLists = consumptionDao.getAll()
}