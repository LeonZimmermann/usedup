package com.hotmail.leon.zimmermann.homeassistant.product

import androidx.room.*

@Entity
data class Product(
    @PrimaryKey val id: Int,
    var name: String,
    var min: Int,
    var max: Int,
    var current: Int
)

@Dao
interface ProductDAO {
    @Query("SELECT * FROM product")
    fun getAll()

    /*
    @Query("SELECT * FROM product WHERE current < min")
    fun getAllWithDeficit()
    */

    @Insert
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)
}