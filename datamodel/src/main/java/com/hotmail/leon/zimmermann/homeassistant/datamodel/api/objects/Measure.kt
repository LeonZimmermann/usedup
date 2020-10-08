package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.exceptions.DataIntegrityException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseId
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseMeasure

data class Measure(
    var id: Id,
    var name: String,
    var abbreviation: String,
    var baseFactor: Float,
    var type: String
) {
    companion object {
        internal fun createInstance(id: String, firebaseObject: FirebaseMeasure): Measure {
            val name = firebaseObject.name ?: throw DataIntegrityException()
            val abbreviation = firebaseObject.abbreviation ?: throw DataIntegrityException()
            val baseFactor = firebaseObject.baseFactor ?: throw DataIntegrityException()
            val type = firebaseObject.type ?: throw DataIntegrityException()
            return Measure(FirebaseId(id), name, abbreviation, baseFactor, type)
        }
    }
}

fun Double.toBase(measure: Measure) = this * measure.baseFactor
fun Double.toMeasure(measure: Measure) = this / measure.baseFactor
