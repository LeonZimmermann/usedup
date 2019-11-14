package com.hotmail.leon.zimmermann.homeassistant.models.tables.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey var id: Int = 0,
    var name: String
)