package com.hotmail.leon.zimmermann.homeassistant.app

import java.text.DecimalFormat

private val FLOATING_POINT_FORMAT = DecimalFormat("0.0#")
fun Double.toFloatFormat(): String = FLOATING_POINT_FORMAT.format(this)

private val INT_FORMAT = DecimalFormat("0")
fun Int.toIntFormat(): String = INT_FORMAT.format(this)