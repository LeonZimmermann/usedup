package com.hotmail.leon.zimmermann.homeassistant.datamodel

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

data class Measure(var name: String = "", var abbreviation: String = "", var baseFactor: Float = 0f) {
    companion object {
        const val COLLECTION_NAME = "measures"
    }
}

object MeasureRepository {
    val measures = mutableListOf<Pair<String, Measure>>()

    fun init() {
        Tasks.await(Firebase.firestore.collection(Measure.COLLECTION_NAME).get()).forEach { document ->
            measures.add(Pair(document.id, document.toObject()))
        }
    }

    fun getId(measureName: String) = measures.first { it.second.name == measureName }.first
    fun getMeasureForId(id: String) = measures.first { it.first == id }.second
    fun getMeasureForName(name: String) = measures.first { it.second.name == name }.second
}