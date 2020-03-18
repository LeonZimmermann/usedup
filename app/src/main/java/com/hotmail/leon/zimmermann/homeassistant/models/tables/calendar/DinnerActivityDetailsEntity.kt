package com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dinner_activity_details")
data class DinnerActivityDetailsEntity(
    @ColumnInfo(name = "dinner_id") var dinnerId: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}