package com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface ConsumptionDao {

    @Query("SELECT * FROM consumption_list")
    fun getAll(): LiveData<List<ConsumptionList>>

}