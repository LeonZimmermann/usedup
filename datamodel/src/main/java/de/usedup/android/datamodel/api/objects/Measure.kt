package de.usedup.android.datamodel.api.objects

import de.usedup.android.datamodel.api.exceptions.DataIntegrityException
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import de.usedup.android.datamodel.firebase.objects.FirebaseMeasure

data class Measure(
  val id: Id,
  var name: String,
  var abbreviation: String,
  var baseFactor: Float,
  var type: String,
  var complex: Boolean
) {
  companion object {
    internal fun createInstance(id: String, firebaseObject: FirebaseMeasure): Measure {
      val name = firebaseObject.name ?: throw DataIntegrityException()
      val abbreviation = firebaseObject.abbreviation ?: throw DataIntegrityException()
      val baseFactor = firebaseObject.baseFactor ?: throw DataIntegrityException()
      val type = firebaseObject.type ?: throw DataIntegrityException()
      val complex = firebaseObject.complex ?: throw DataIntegrityException()
      return Measure(FirebaseId(id), name, abbreviation, baseFactor, type, complex)
    }
  }
}

fun Double.toBase(measure: Measure) = this * measure.baseFactor
fun Double.toMeasure(measure: Measure) = this / measure.baseFactor
