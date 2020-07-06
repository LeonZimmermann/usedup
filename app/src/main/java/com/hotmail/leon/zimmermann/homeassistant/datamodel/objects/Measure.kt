package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

class Measure(
    @DocumentId private val _id: String? = null,
    @PropertyName("name") private var _name: String? = null,
    @PropertyName("abbreviation") private var _abbreviation: String? = null,
    @PropertyName("baseFactor") private var _baseFactor: Float? = null
) {
    val id: String
        get() = _id!!
    var name: String
        set(value) {
            _name = value
        }
        get() = _name!!
    var abbreviation: String
        set(value) { _abbreviation = value }
        get() = _abbreviation!!
    var baseFactor: Float
        set(value) { _baseFactor = value }
        get() = _baseFactor!!

    companion object {
        const val COLLECTION_NAME = "measures"
    }
}

fun Double.toBase(measure: Measure) = this * measure.baseFactor
fun Double.toMeasure(measure: Measure) = this / measure.baseFactor
