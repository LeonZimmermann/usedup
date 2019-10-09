package com.hotmail.leon.zimmermann.homeassistant.models.tables.measure

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MeasureDao {

    @Query("SELECT * FROM measures")
    fun getAll(): LiveData<List<MeasureEntity>>

    @Query("SELECT * FROM measures WHERE id = :id")
    suspend fun get(id: Int): LiveData<MeasureEntity>

    @Insert
    suspend fun insert(measureEntity: MeasureEntity)
}
