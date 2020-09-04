package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.CategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import kotlin.math.floor
import kotlin.math.max

@IgnoreExtraProperties
class Product(
    @PropertyName("name") private var _name: String? = null,
    @PropertyName("quantity") private var _quantity: Double? = null,
    @PropertyName("min") private var _min: Int? = null,
    @PropertyName("max") private var _max: Int? = null,
    @PropertyName("capacity") private var _capacity: Double? = null,
    @PropertyName("measure") private var _measure: DocumentReference? = null,
    @PropertyName("category") private var _category: DocumentReference? = null
) {
    @DocumentId lateinit var id: String
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
    var measure: Measure
        set(value) {
            _measure = Firebase.firestore.collection(Measure.COLLECTION_NAME).document(value.id)
        }
        get() = MeasureRepository.getMeasureForId(_measure!!.id)
    var category: Category
        set(value) {
            _category = Firebase.firestore.collection(Category.COLLECTION_NAME).document(value.id)
        }
        get() = CategoryRepository.getCategoryForId(_category!!.id)

    val discrepancy: Int
        get() = max(max - floor(quantity).toInt(), 0)

    companion object {
        const val COLLECTION_NAME = "products"
    }
}