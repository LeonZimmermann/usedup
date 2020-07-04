package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

class Category(
    @DocumentId private val _id: String? = null,
    @PropertyName("name") private var _name: String? = null,
    @PropertyName("position") private var _position: Int? = null
) {
    val id: String
        get() = _id!!
    var name: String
        set(value) {
            _name = value
        }
        get() = _name!!
    var position: Int
        set(value) {
            _position = value
        }
        get() = _position!!

    companion object {
        const val COLLECTION_NAME = "categories"
    }
}