package com.hotmail.leon.zimmermann.homeassistant.models.tables.measure

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "measures")
data class MeasureEntity(
    @PrimaryKey
    @ColumnInfo(name = "measure_id")
    val id: Long,
    var text: String,
    var abbreviation: String
)