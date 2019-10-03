package com.hotmail.leon.zimmermann.homeassistant.models.tables.packaging

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface PackagingDao {

    @Query("SELECT * FROM packagings")
    fun getAll(): LiveData<List<PackagingEntity>>

}