package com.hotmail.leon.zimmermann.homeassistant.product

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAll() : LiveData<List<Product>>

    @Insert
    suspend fun insert(product: Product)

    @Insert
    suspend fun insertAll(productList: List<Product>)

    @Update
    suspend fun update(product: Product)

    @Update
    suspend fun updateAll(productList: List<Product>)

    @Delete
    suspend fun delete(product: Product)

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}