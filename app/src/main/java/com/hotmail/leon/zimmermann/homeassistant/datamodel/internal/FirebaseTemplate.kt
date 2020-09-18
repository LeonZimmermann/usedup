package com.hotmail.leon.zimmermann.homeassistant.datamodel.internal

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

class FirebaseTemplate(
    @PropertyName("name") var name: String? = null,
    @PropertyName("components") var components: List<FirebaseTemplateComponent>? = null
) {
    companion object {
        const val COLLECTION_NAME = "templates"
    }
}

class FirebaseTemplateComponent(
    @PropertyName("productReference") var product: DocumentReference? = null,
    @PropertyName("measureReference") var measure: DocumentReference? = null,
    @PropertyName("value") var value: Double? = null
)