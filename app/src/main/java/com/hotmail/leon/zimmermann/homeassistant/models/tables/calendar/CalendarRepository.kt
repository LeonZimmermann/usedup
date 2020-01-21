package com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar

class CalendarRepository(private val dao: CalendarDao) {
    val calendarActivities = dao.getAllCalendarActivities()
    val details = listOf(CalendarActivityType.COOKING to dao.getAllCookingActivityDetails())

    suspend fun insertCalendarActivity(calendarActivity: CalendarActivityEntity) {
        dao.insertCalendarActivity(calendarActivity)
    }
}