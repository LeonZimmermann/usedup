package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

class Template(
    @PropertyName("") private var _name: String? = null,
    @PropertyName("") private var _components: List<TemplateComponent>? = null
) {
    @DocumentId lateinit var id: String
    var name: String
        set(value) {
            _name = value
        }
        get() = _name!!
    var components: List<TemplateComponent>
        set(value) {
            _components = value
        }
        get() = _components!!

    companion object {
        const val COLLECTION_NAME = "templates"
    }
}

class TemplateComponent(
    product: DocumentReference? = null,
    measure: DocumentReference? = null,
    value: Double? = null
) {
    var product: DocumentReference = product!!
    var measure: DocumentReference = measure!!
    var value: Double = value!!
}