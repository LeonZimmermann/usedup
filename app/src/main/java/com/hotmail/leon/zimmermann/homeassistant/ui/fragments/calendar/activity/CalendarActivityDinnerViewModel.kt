package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity

import android.app.Application
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar.CalendarActivityEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar.CalendarRepository
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionList
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*

class CalendarActivityDinnerViewModel(application: Application) : AndroidViewModel(application) {
    private val dateFormatter = DateFormat.getDateInstance()
    private val timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT)

    val date: MutableLiveData<Date> by lazy { MutableLiveData<Date>(Date()) }
    val dateString: LiveData<String> = Transformations.map(date) {
        if (date.value != null) dateFormatter.format(date.value!!) else "Date"
    }
    val timeString: LiveData<String> = Transformations.map(date) {
        if (date.value != null) timeFormatter.format(date.value!!) else "Time"
    }

    var consumptionList: ConsumptionList? = null
    private val calendarRepository: CalendarRepository

    init {
        val database = HomeAssistantDatabase.getDatabase(application, viewModelScope)
        calendarRepository = CalendarRepository(database.calendarDao())
        calendarRepository.calendarActivities
    }

    fun insertCalendarActivity(calendarActivities: CalendarActivityEntity) {
        viewModelScope.launch {
            calendarRepository.insertCalendarActivity(calendarActivities)
        }
    }

}
