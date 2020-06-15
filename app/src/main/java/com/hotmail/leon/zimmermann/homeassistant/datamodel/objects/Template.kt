package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

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

object TemplateRepository {
    val templates: MutableList<Pair<String, Template>> by lazy {
        val list: MutableList<Pair<String, Template>> = mutableListOf()
        Firebase.firestore.collection(Template.COLLECTION_NAME).get().addOnSuccessListener { documents ->
            for (document in documents)
                list.add(Pair(document.id, document.toObject()))
        }
        list
    }

    fun getId(templateName: String) = templates.first { it.second.name == templateName }.first
    fun getTemplateForId(id: String) = templates.first { it.first == id }.second
    fun getTemplateForName(name: String) = templates.first { it.second.name == name }.second
}