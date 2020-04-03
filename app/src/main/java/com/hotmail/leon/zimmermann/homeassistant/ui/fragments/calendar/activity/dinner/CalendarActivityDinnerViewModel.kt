package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity.dinner

import android.app.Application
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar.CalendarActivityEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar.CalendarActivityType
import com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar.CalendarRepository
import com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar.DinnerActivityDetailsEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.meal.Meal
import com.hotmail.leon.zimmermann.homeassistant.models.tables.meal.MealRepository
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

    var meal: Meal? = null
    private val calendarRepository: CalendarRepository

    private val mealRepository: MealRepository
    val mealList: LiveData<List<Meal>>

    init {
        val database = HomeAssistantDatabase.getDatabase(application, viewModelScope)
        calendarRepository = CalendarRepository(database.calendarDao())
        calendarRepository.calendarActivities

        mealRepository = MealRepository(database.mealDao())
        mealList = mealRepository.mealList
    }

    fun insertCalendarActivity() {
        viewModelScope.launch {
            val detailsId = calendarRepository.insertDinnerActivityDetails(
                DinnerActivityDetailsEntity(meal!!.id)
            )
            // TODO Convert all Date usages to the Java 8 Time Standard Library
            val date = java.sql.Date(date.value!!.time)
            calendarRepository.insertCalendarActivity(
                CalendarActivityEntity(
                    date, date,
                    CalendarActivityType.COOKING.id, detailsId
                )
            )
        }
    }
}
