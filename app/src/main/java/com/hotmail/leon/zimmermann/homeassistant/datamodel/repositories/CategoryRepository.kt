package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Category

object CategoryRepository {
    val categories = mutableListOf<Category>()

    fun init() {
        Tasks.await(Firebase.firestore.collection(Category.COLLECTION_NAME).get()).forEach { document ->
            categories.add(document.toObject())
        }
    }

    fun getCategoryForId(id: String) = categories.first { it.id == id }
    fun getCategoryForName(name: String) = categories.first { it.name == name }
}