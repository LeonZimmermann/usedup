package com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar

import com.hotmail.leon.zimmermann.homeassistant.R

enum class CalendarActivityType(val icon: Int) {
    SHOPPING(R.drawable.cart_icon),
    COOKING(R.drawable.cart_icon),
    EATING(R.drawable.cart_icon);

    val id: Int = ordinal
}