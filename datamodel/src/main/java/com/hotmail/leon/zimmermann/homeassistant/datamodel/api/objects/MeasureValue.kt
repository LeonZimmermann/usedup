package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.exceptions.IncompatibleMeasuresException

data class MeasureValue(var double: Double, var measure: Measure) {
    operator fun plus(other: MeasureValue): MeasureValue {
        if (other.measure.type != measure.type) throw IncompatibleMeasuresException()
        val baseValue = this.double.toBase(this.measure)
        val otherBaseValue = other.double.toBase(other.measure)
        return MeasureValue((baseValue + otherBaseValue).toMeasure(measure), measure)
    }

    operator fun minus(other: MeasureValue): MeasureValue {
        if (other.measure.type != measure.type) throw IncompatibleMeasuresException()
        val baseValue = this.double.toBase(this.measure)
        val otherBaseValue = other.double.toBase(other.measure)
        return MeasureValue((baseValue - otherBaseValue).toMeasure(measure), measure)
    }

    override fun toString(): String {
        return double.toString() + measure.abbreviation
    }
}