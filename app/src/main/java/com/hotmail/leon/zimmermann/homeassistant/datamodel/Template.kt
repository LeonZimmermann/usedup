package com.hotmail.leon.zimmermann.homeassistant.datamodel

import com.google.firebase.firestore.DocumentReference

data class Template(
    val name: String? = null,
    val components: List<TemplateComponent>? = null
) {
    companion object {
        const val COLLECTION_NAME = "templates"
    }
}

data class TemplateComponent(
    val product: DocumentReference? = null,
    val measure: DocumentReference? = null,
    val value: Double? = null
)