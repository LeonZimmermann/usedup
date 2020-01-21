package com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CalendarDao {

    @Query("SELECT * FROM calendar_activities")
    fun getAllCalendarActivities(): LiveData<List<CalendarActivityEntity>>

    @Query("SELECT * FROM cooking_activity_details")
    fun getAllCookingActivityDetails(): LiveData<List<CookingActivityDetailsEntity>>

    @Insert
    suspend fun insertCalendarActivity(calendarActivity: CalendarActivityEntity)

    @Insert
    suspend fun insertCalendarActivities(calendarActivity: List<CalendarActivityEntity>)

    @Insert
    suspend fun insertCookingActivityDetails(cookingActivityDetails: List<CookingActivityDetailsEntity>)
}