package com.hotmail.leon.zimmermann.homeassistant.models.tables.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "category_id")
    var id: Long,
    var name: String,
    var position: Int
)