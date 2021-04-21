package de.usedup.android.datamodel.api

import kotlin.math.floor
import kotlin.math.max

fun calculateRawDiscrepancy(targetValue: Int, quantity: Double) = targetValue - floor(quantity).toInt()

fun calculateDiscrepancy(targetValue: Int, quantity: Double) = max(calculateRawDiscrepancy(targetValue, quantity), 0)

