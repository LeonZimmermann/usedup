package com.hotmail.leon.zimmermann.homeassistant.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAll() : LiveData<List<Product>>

    @Insert
    suspend fun insert(product: Product)

    @Insert
    suspend fun insertAll(productList: List<Product>)

    @Delete
    suspend fun delete(product: Product)

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}