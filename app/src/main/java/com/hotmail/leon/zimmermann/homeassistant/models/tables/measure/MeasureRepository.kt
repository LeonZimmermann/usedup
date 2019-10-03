package com.hotmail.leon.zimmermann.homeassistant.models.tables.measure

class MeasureRepository(private val measureDao: MeasureDao) {
    val measureEntityList = measureDao.getAll()

    suspend fun insert(measureEntity: MeasureEntity) {
        measureDao.insert(measureEntity)
    }
}