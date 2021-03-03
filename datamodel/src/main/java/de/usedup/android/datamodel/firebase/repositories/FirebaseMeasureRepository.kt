package de.usedup.android.datamodel.firebase.repositories

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Measure
import de.usedup.android.datamodel.api.repositories.MeasureRepository
import de.usedup.android.datamodel.firebase.objects.FirebaseMeasure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FirebaseMeasureRepository : MeasureRepository {
  override val measures = mutableListOf<Measure>()

  override suspend fun init() {
    withContext(Dispatchers.IO) {
      measures.clear()
      Tasks.await(Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).get()).forEach { document ->
        measures.add(Measure.createInstance(document.id, document.toObject()))
      }
    }
  }

  override fun getMeasureForId(id: Id) = measures.firstOrNull { it.id == id }
  override fun getMeasureForName(name: String) = measures.firstOrNull { it.name == name }
}