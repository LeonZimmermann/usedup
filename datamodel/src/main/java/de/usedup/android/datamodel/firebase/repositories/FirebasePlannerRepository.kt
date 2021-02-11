package de.usedup.android.datamodel.firebase.repositories

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.PlannerItem
import de.usedup.android.datamodel.api.repositories.PlannerRepository
import de.usedup.android.datamodel.firebase.filterForUser
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import de.usedup.android.datamodel.firebase.objects.FirebaseMeal
import de.usedup.android.datamodel.firebase.objects.FirebasePlannerItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException

object FirebasePlannerRepository : PlannerRepository {

  override val plan: MutableLiveData<MutableList<PlannerItem>> = MutableLiveData()
  private val collection = Firebase.firestore.collection(FirebasePlannerItem.COLLECTION_NAME)

  override suspend fun init() {
    withContext(Dispatchers.IO) {
      collection.filterForUser().get()
        .addOnSuccessListener { documents ->
          plan.value = documents.map { PlannerItem.createInstance(it.id, it.toObject()) }.toMutableList()
        }
    }
  }

  override fun getAllPlannerItems(): List<PlannerItem> = runBlocking(Dispatchers.IO) {
    if (plan.value != null) plan.value!!
    else Tasks.await(collection.filterForUser().get()).map { PlannerItem.createInstance(it.id, it.toObject()) }
  }

  private suspend fun getPlannerItemForId(id: Id): PlannerItem = withContext(Dispatchers.IO) {
    if (plan.value != null) plan.value!!.first { it.id == id }
    else {
      val document = Tasks.await(collection.document((id as FirebaseId).value).get())
      val firebasePlannerItem = document.toObject<FirebasePlannerItem>() ?: throw IOException()
      val plannerItem = PlannerItem.createInstance(document.id, firebasePlannerItem)
      val plannerItemList = plan.value!!
      plannerItemList.add(plannerItem)
      plan.postValue(plannerItemList)
      plannerItem
    }
  }

  override suspend fun addPlannerItem(mealId: Id, date: Long) = withContext(Dispatchers.IO) {
    val mealReference =
      Firebase.firestore.collection(FirebaseMeal.COLLECTION_NAME).document((mealId as FirebaseId).value)
    val firebaseObject =
      FirebasePlannerItem(mealReference, date, FirebaseUserRepository.getDocumentReferenceToCurrentUser())
    val task = collection.add(firebaseObject).apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception)
    else {
      val plannerItemList = plan.value!!
      plannerItemList.add(PlannerItem.createInstance(task.result!!.id, firebaseObject))
      plan.postValue(plannerItemList)
    }
  }

  override suspend fun updatePlannerItem(id: Id, mealId: Id, date: Long) = withContext(Dispatchers.IO) {
    val data = mapOf(
      "mealReference" to Firebase.firestore.collection(FirebaseMeal.COLLECTION_NAME)
        .document((mealId as FirebaseId).value),
      "date" to date
    )
    val task = collection.document((id as FirebaseId).value).update(data).apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception)
    else {
      getPlannerItemForId(id).apply {
        this.mealId = mealId
        this.date = date
      }
      plan.postValue(plan.value)
    }
  }

  override suspend fun deletePlannerItem(id: Id) = withContext(Dispatchers.IO) {
    plan.value!!.remove(getPlannerItemForId(id))
    val task = collection.document((id as FirebaseId).value).delete().apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception)
    else plan.postValue(plan.value)
  }
}