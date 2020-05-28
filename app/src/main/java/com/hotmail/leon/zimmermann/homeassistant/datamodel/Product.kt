package com.hotmail.leon.zimmermann.homeassistant.datamodel

import com.google.firebase.firestore.DocumentReference
import kotlin.math.floor
import kotlin.math.max

data class Product(
    var name: String = "",
    var quantity: Double = 0.0,
    var min: Int = 0,
    var max: Int = 0,
    var capacity: Double = 0.0,
    var measure: DocumentReference? = null,
    var category: DocumentReference? = null
) {
    val discrepancy: Int
        get() = max(max - floor(quantity).toInt(), 0)

    companion object {
        const val COLLECTION_NAME = "products"
    }
}