package com.hotmail.leon.zimmermann.homeassistant.datamodel.internal

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class FirebaseProduct(
    @PropertyName("name") var name: String? = null,
    @PropertyName("quantity") var quantity: Double? = null,
    @PropertyName("min") var min: Int? = null,
    @PropertyName("max") var max: Int? = null,
    @PropertyName("capacity") var capacity: Double? = null,
    @PropertyName("measureReference") var measureReference: DocumentReference? = null,
    @PropertyName("categoryReference") var categoryReference: DocumentReference? = null
) {
    companion object {
        const val COLLECTION_NAME = "products"
    }
}