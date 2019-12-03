package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionList
import java.util.*

class CalendarActivityDinnerViewModel : ViewModel() {
    val date: MutableLiveData<Date> by lazy { MutableLiveData<Date>(Date()) }
    var consumptionList: ConsumptionList? = null
}
