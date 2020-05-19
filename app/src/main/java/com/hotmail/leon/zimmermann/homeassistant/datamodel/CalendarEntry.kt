package com.hotmail.leon.zimmermann.homeassistant.datamodel

import com.hotmail.leon.zimmermann.homeassistant.R
import java.sql.Date

data class CalendarEntry(
    var dateFrom: Date,
    var dateTo: Date,
    var type: CalendarEntryType,
    var details: CalendarTypeDetails
)

enum class CalendarEntryType(val icon: Int) {
    SHOPPING(R.drawable.cart_icon),
    COOKING(R.drawable.cart_icon);

    val id: Int = ordinal
}

sealed class CalendarTypeDetails
class CalendarMealEntryDetails(var meal: Meal): CalendarTypeDetails()
class CalendarShoppingEntryDetails: CalendarTypeDetails()