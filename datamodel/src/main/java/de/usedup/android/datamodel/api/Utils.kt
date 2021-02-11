package de.usedup.android.datamodel.api

import kotlin.math.floor
import kotlin.math.max

fun calculateDiscrepancy(min: Int, quantity: Double) = max(min - floor(quantity).toInt(), 0)