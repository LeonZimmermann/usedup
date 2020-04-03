package com.hotmail.leon.zimmermann.homeassistant.models.tables.meal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "meals")
data class MealEntity(
    var name: String,
    var duration: Int? = null,
    var description: String? = null,
    var instructions: String? = null,
    var backgroundUrl: String? = null
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "meal_id")
    var id: Long = 0
}