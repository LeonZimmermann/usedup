package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseMeasure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Measure

object MeasureRepository {
    val measures = mutableListOf<Measure>()

    fun init() {
        Tasks.await(Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).get()).forEach { document ->
            measures.add(Measure.createInstance(document.id, document.toObject()))
        }
    }

    fun getMeasureForId(id: String) = measures.first { it.id == id }
    fun getMeasureForName(name: String) = measures.first { it.name == name }
}