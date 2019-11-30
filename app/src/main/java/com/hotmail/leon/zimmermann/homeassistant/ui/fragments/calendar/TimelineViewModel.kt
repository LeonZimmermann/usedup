package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar.CalendarActivityEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar.CalendarRepository

class TimelineViewModel(application: Application) : AndroidViewModel(application) {

    val calendarActivities: LiveData<List<CalendarActivityEntity>>

    init {
        val database = HomeAssistantDatabase.getDatabase(application, viewModelScope)
        val calendarDao = database.calendarDao()
        val calendarRepository = CalendarRepository(calendarDao)
        calendarActivities = calendarRepository.calendarActivities
    }
}
