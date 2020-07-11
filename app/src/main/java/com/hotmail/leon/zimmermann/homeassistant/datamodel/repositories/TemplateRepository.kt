package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
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

    fun addTemplate(name: String, consumptionList: List<TemplateComponent>): Task<DocumentReference> {
        val template = Template(name, consumptionList)
        return database.collection(Template.COLLECTION_NAME)
            .add(template)
            .addOnSuccessListener {
                template.id = it.id
                templates.add(template)
            }
    }

    fun updateTemplate(templateId: String, name: String, components: List<TemplateComponent>): Task<Void> {
        val data = mapOf(
            "name" to name,
            "components" to components
        )
        return database.collection(Template.COLLECTION_NAME)
            .document(templateId)
            .update(data)
            .addOnSuccessListener {
                getTemplateForId(templateId).apply {
                    this.name = name
                    this.components = components
                }
            }
    }

    fun deleteTemplate(templateId: String): Task<Void> {
        templates.remove(getTemplateForId(templateId))
        return database.collection(Template.COLLECTION_NAME).document(templateId).delete()
    }
}