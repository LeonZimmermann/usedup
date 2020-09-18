package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.DataIntegrityException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseMeasure

class Measure(
    var id: String,
    var name: String,
    var abbreviation: String,
    var baseFactor: Float
) {
    companion object {
        fun createInstance(id: String, firebaseObject: FirebaseMeasure): Measure {
            val name = firebaseObject.name ?: throw DataIntegrityException()
            val abbreviation = firebaseObject.abbreviation ?: throw DataIntegrityException()
            val baseFactor = firebaseObject.baseFactor ?: throw DataIntegrityException()
            return Measure(id, name, abbreviation, baseFactor)
        }
    }
}

fun Double.toBase(measure: Measure) = this * measure.baseFactor
fun Double.toMeasure(measure: Measure) = this / measure.baseFactor
