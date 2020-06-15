package com.hotmail.leon.zimmermann.homeassistant.datamodel

import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Measure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.toBase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.toMeasure

data class Value(var double: Double, var measure: Measure) {
    operator fun plus(other: Value): Value {
        // TODO Check if measures are compatible
        val baseValue = this.double.toBase(this.measure)
        val otherBaseValue = other.double.toBase(other.measure)
        return Value((baseValue + otherBaseValue).toMeasure(measure), measure)
    }

    override fun toString(): String {
        return double.toString() + measure.abbreviation
    }
}