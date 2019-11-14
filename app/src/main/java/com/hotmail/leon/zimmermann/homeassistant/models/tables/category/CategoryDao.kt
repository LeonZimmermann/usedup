package com.hotmail.leon.zimmermann.homeassistant.models.tables.category

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getAll(): LiveData<List<CategoryEntity>>

    @Insert
    suspend fun insert(categoryEntity: CategoryEntity)
}