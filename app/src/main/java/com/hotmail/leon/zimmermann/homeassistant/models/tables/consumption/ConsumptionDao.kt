package com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ConsumptionDao {

    @Query("SELECT * FROM consumption")
    fun getAll(): List<ConsumptionList>

    @Insert
    suspend fun insert(consumptionEntityList: List<ConsumptionEntity>)
}