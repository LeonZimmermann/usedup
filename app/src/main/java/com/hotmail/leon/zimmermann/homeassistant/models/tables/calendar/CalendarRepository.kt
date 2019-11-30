package com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar

class CalendarRepository(dao: CalendarDao) {
    val calendarActivities = dao.getAllCalendarActivities()
}