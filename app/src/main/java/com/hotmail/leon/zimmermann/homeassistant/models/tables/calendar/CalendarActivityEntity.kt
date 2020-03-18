package com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "calendar_activities")
data class CalendarActivityEntity(
    var dateFrom: Date,
    var dateTo: Date,
    @ColumnInfo(name = "type_id") var typeId: Int,
    @ColumnInfo(name = "details_id") var detailsId: Long?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}