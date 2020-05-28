package com.hotmail.leon.zimmermann.homeassistant.datamodel

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

data class Category(var name: String = "", var position: Int = 0) {
    companion object {
        const val COLLECTION_NAME = "categories"
    }
}

object CategoryRepository {
    val categories = mutableListOf<Pair<String, Category>>()

    fun init() {
        Tasks.await(Firebase.firestore.collection(Category.COLLECTION_NAME).get()).forEach { document ->
            categories.add(Pair(document.id, document.toObject()))
        }
    }

    fun getId(categoryName: String) = categories.first { it.second.name == categoryName }.first
    fun getCategoryForId(id: String) = categories.first { it.first == id }.second
    fun getCategoryForName(name: String) = categories.first { it.second.name == name }.second
}