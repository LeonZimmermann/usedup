package com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class ConsumptionDao {

    @Query("SELECT * FROM consumption_list")
    abstract fun getAll(): LiveData<List<ConsumptionList>>

    suspend fun insert(consumptionList: ConsumptionList) {
        insert(consumptionList.metaData)
        insert(consumptionList.consumptions)
    }

    @Insert
    protected abstract suspend fun insert(consumptionEntityList: List<ConsumptionEntity>)

    @Insert
    protected abstract suspend fun insert(consumptionListMetaDataEntity: ConsumptionListMetaDataEntity)

}