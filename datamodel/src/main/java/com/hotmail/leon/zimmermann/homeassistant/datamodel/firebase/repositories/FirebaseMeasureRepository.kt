package com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseMeasure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Measure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MeasureRepository

object FirebaseMeasureRepository : MeasureRepository {
  override val measures = mutableListOf<Measure>()

  override fun init() {
    Tasks.await(Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).get()).forEach { document ->
      measures.add(Measure.createInstance(document.id, document.toObject()))
    }
  }

  override fun getMeasureForId(id: Id) = measures.first { it.id == id }
  override fun getMeasureForName(name: String) = measures.first { it.name == name }
}