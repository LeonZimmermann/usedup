package com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CalendarDao {

    @Query("SELECT * FROM calendar_activities")
    fun getAllCalendarActivities(): LiveData<List<CalendarActivityEntity>>

    @Query("SELECT * FROM dinner_activity_details")
    fun getAllCookingActivityDetails(): LiveData<List<DinnerActivityDetailsEntity>>

    @Insert
    suspend fun insertCalendarActivity(calendarActivity: CalendarActivityEntity)

    @Insert
    suspend fun insertCalendarActivities(calendarActivity: List<CalendarActivityEntity>)

    @Insert
    suspend fun insertDinnerActivityDetails(dinnerActivityDetails: DinnerActivityDetailsEntity): Long

    @Insert
    suspend fun insertDinnerActivityDetailsList(dinnerActivityDetails: List<DinnerActivityDetailsEntity>)
}