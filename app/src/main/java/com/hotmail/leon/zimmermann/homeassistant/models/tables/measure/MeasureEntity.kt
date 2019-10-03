package com.hotmail.leon.zimmermann.homeassistant.models.tables.measure

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "measures")
data class MeasureEntity(
    @PrimaryKey val id: Int,
    var text: String,
    var abbreviation: String
)