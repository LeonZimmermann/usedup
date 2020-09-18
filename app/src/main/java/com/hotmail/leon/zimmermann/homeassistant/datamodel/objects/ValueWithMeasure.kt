package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

data class ValueWithMeasure(var double: Double, var measure: Measure) {
    operator fun plus(other: ValueWithMeasure): ValueWithMeasure {
        // TODO Check if measures are compatible
        val baseValue = this.double.toBase(this.measure)
        val otherBaseValue = other.double.toBase(other.measure)
        return ValueWithMeasure(
            (baseValue + otherBaseValue).toMeasure(
                measure
            ), measure
        )
    }

    override fun toString(): String {
        return double.toString() + measure.abbreviation
    }
}