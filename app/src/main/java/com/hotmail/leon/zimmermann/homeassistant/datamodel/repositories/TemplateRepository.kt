package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseMeasure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseTemplate
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseTemplateComponent
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Template
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.TemplateComponent

object TemplateRepository {
    private val database = Firebase.firestore

    val templates: MutableList<Template> = mutableListOf()

    fun init() {
        Tasks.await(database.collection(FirebaseTemplate.COLLECTION_NAME).get()).forEach { document ->
            templates.add(Template.createInstance(document.id, document.toObject()))
        }
    }

    fun getTemplateForId(id: String) = templates.first { it.id == id }
    fun getTemplateForName(name: String) = templates.first { it.name == name }

    fun addTemplate(name: String, components: List<TemplateComponent>): Task<DocumentReference> {
        val firebaseComponents = components.map {
            FirebaseTemplateComponent(
                Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME).document(it.productId),
                Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).document(it.measureId),
                it.value
            )
        }
        val firebaseTemplate = FirebaseTemplate(name, firebaseComponents)
        return database.collection(FirebaseTemplate.COLLECTION_NAME)
            .add(firebaseTemplate)
            .addOnSuccessListener {
                templates.add(Template.createInstance(it.id, firebaseTemplate))
            }
    }

    fun updateTemplate(templateId: String, name: String, components: List<TemplateComponent>): Task<Void> {
        val firebaseComponents = components.map {
            FirebaseTemplateComponent(
                Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME).document(it.productId),
                Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).document(it.measureId),
                it.value
            )
        }
        val data = mapOf(
            "name" to name,
            "components" to firebaseComponents
        )
        return database.collection(FirebaseTemplate.COLLECTION_NAME)
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
        return database.collection(FirebaseTemplate.COLLECTION_NAME).document(templateId).delete()
    }
}