package com.hotmail.leon.zimmermann.homeassistant.models.tables.category

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getAll(): LiveData<List<CategoryEntity>>

    @Query("SELECT * FROM categories")
    suspend fun getAllStatically(): List<CategoryEntity>

    @Insert
    suspend fun insert(categoryEntity: CategoryEntity)

    @Update
    suspend fun update(categoryList: List<CategoryEntity>)
}