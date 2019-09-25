package com.hotmail.leon.zimmermann.homeassistant.models.product

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAll() : LiveData<List<ProductEntity>>

    @Insert
    suspend fun insert(productEntity: ProductEntity)

    @Insert
    suspend fun insertAll(productEntityList: List<ProductEntity>)

    @Update
    suspend fun update(productEntity: ProductEntity)

    @Update
    suspend fun updateAll(productEntityList: List<ProductEntity>)

    @Delete
    suspend fun delete(productEntity: ProductEntity)

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}