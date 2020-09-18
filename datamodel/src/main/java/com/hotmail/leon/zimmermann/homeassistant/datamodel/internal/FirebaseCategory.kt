package com.hotmail.leon.zimmermann.homeassistant.datamodel.internal

import com.google.firebase.firestore.PropertyName

data class FirebaseCategory(
    @PropertyName("name") var name: String? = null,
    @PropertyName("position") var position: Int? = null
) {
    companion object {
        const val COLLECTION_NAME = "categories"
    }
}