package de.usedup.android.datamodel.api

import kotlin.math.floor
import kotlin.math.max
fun calculateDiscrepancy(targetValue: Int, quantity: Double) = max(targetValue - floor(quantity).toInt(), 0)

