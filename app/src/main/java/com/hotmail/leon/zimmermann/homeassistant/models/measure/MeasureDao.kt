package com.hotmail.leon.zimmermann.homeassistant.models.measure

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MeasureDao {

    @Query("SELECT * FROM measures")
    fun getAll(): LiveData<List<MeasureEntity>>

    @Insert
    suspend fun insert(measureEntity: MeasureEntity)
}
