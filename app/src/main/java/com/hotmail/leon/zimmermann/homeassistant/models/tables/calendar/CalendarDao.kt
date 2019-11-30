package com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CalendarDao {

    @Query("SELECT * FROM calendar_activities")
    fun getAllCalendarActivities(): LiveData<List<CalendarActivityEntity>>

    @Insert
    suspend fun insertCalendarActivities(calendarActivities: List<CalendarActivityEntity>)
}