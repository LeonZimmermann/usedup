package com.hotmail.leon.zimmermann.homeassistant.product

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlin.math.max

@Entity(tableName = "products")
data class Product(
    var name: String,
    var quantity: Int,
    var min: Int,
    var max: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    val discrepancy: Int
        get() = max(max - quantity, 0)
}