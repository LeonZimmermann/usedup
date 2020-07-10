package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Template
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.TemplateComponent

object TemplateRepository {
    private val database = Firebase.firestore

    val templates: MutableList<Template> by lazy {
        val list: MutableList<Template> = mutableListOf()
        database.collection(Template.COLLECTION_NAME).get().addOnSuccessListener { documents ->
            for (document in documents) list.add(document.toObject())
        }
        list
    }

    fun getTemplateForId(id: String) = templates.first { it.id == id }
    fun getTemplateForName(name: String) = templates.first { it.name == name }

    fun addTemplate(name: String, consumptionList: List<TemplateComponent>) {
        val template = Template(name, consumptionList)
        database.collection(Template.COLLECTION_NAME)
            .add(template)
    }

    fun deleteTemplate(id: String) {
        TODO("Implement")
    }
}