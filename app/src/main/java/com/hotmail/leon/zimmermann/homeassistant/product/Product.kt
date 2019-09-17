package com.hotmail.leon.zimmermann.homeassistant.product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    var name: String,
    var quantity: Int,
    var min: Int,
    var max: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}