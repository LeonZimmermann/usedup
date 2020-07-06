package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import kotlin.math.floor
import kotlin.math.max

class Product(
    @DocumentId private val _id: String? = null,
    @PropertyName("name") private var _name: String? = null,
    @PropertyName("quantity") private var _quantity: Double? = null,
    @PropertyName("min") private var _min: Int? = null,
    @PropertyName("max") private var _max: Int? = null,
    @PropertyName("capacity") private var _capacity: Double? = null,
    @PropertyName("measure") private var _measure: DocumentReference? = null,
    @PropertyName("category") private var _category: DocumentReference? = null
) {
    val id: String
        get() = _id!!
    var name: String
        set(value) {
            _name = value
        }
        get() = _name!!
    var quantity: Double
        set(value) {
            _quantity = value
        }
        get() = _quantity!!
    var min: Int
        set(value) {
            _min = value
        }
        get() = _min!!
    var max: Int
        set(value) {
            _max = value
        }
        get() = _max!!
    var capacity: Double
        set(value) {
            _capacity = value
        }
        get() = _capacity!!
    var measure: DocumentReference
        set(value) {
            _measure = value
        }
        get() = _measure!!
    var category: DocumentReference
        set(value) {
            _category = value
        }
        get() = _category!!

    val discrepancy: Int
        get() = max(max - floor(quantity).toInt(), 0)

    companion object {
        const val COLLECTION_NAME = "products"
    }
}