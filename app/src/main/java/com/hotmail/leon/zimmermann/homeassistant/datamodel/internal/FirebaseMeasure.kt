package com.hotmail.leon.zimmermann.homeassistant.datamodel.internal

import com.google.firebase.firestore.PropertyName

class FirebaseMeasure(
    @PropertyName("name") var name: String? = null,
    @PropertyName("abbreviation") var abbreviation: String? = null,
    @PropertyName("baseFactor") var baseFactor: Float? = null
) {
    companion object {
        const val COLLECTION_NAME = "measures"
    }
}