package de.usedup.android.datamodel.firebase.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.PlannerItem
import de.usedup.android.datamodel.api.repositories.PlannerRepository
import de.usedup.android.datamodel.firebase.filterForHousehold
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import de.usedup.android.datamodel.firebase.objects.FirebaseMeal
import de.usedup.android.datamodel.firebase.objects.FirebasePlannerItem
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

object FirebasePlannerRepository : PlannerRepository {

  private val collection = Firebase.firestore.collection(FirebasePlannerItem.COLLECTION_NAME)

  private val plannerList: MutableLiveData<MutableList<PlannerItem>> = MutableLiveData()

  override fun getAllPlannerItemsLiveData(coroutineScope: CoroutineScope): LiveData<List<PlannerItem>> {
    if (plannerList.value == null) {
      coroutineScope.launch(Dispatchers.IO) {
        plannerList.postValue(
          Tasks.await(collection.filterForHousehold().get()).map { PlannerItem.createInstance(it.id, it.toObject()) }
            .toMutableList())
      }
    }
    return Transformations.map(plannerList) { it.toList() }
  }

  override fun getAllPlannerItems(): Single<List<PlannerItem>> {
    return Single.fromCallable {
      if (plannerList.value == null) {
        val plannerList =
          Tasks.await(collection.filterForHousehold().get()).map { PlannerItem.createInstance(it.id, it.toObject()) }
            .toMutableList()
        this.plannerList.postValue(plannerList)
        plannerList
      } else {
        plannerList.value
      }
    }
  }

  private suspend fun getPlannerItemForId(id: Id): PlannerItem? = withContext(Dispatchers.IO) {
    if (plannerList.value != null) plannerList.value?.firstOrNull { it.id == id }
    else {
      val document = Tasks.await(collection.document((id as FirebaseId).value).get())
      val firebasePlannerItem = document.toObject<FirebasePlannerItem>() ?: throw IOException()
      val plannerItem = PlannerItem.createInstance(document.id, firebasePlannerItem)
      val plannerItemList = requireNotNull(plannerList.value)
      plannerItemList.add(plannerItem)
      plannerList.postValue(plannerItemList)
      plannerItem
    }
  }

  override suspend fun addPlannerItem(mealId: Id, date: Long) = withContext(Dispatchers.IO) {
    val mealReference =
      Firebase.firestore.collection(FirebaseMeal.COLLECTION_NAME).document((mealId as FirebaseId).value)
    val firebasePlannerItem =
      FirebasePlannerItem(mealReference, date, FirebaseUserRepository.getDocumentReferenceToCurrentUser())
    val task = collection.add(firebasePlannerItem).apply { Tasks.await(this) }
    when {
      task.exception != null -> {
        throw IOException(task.exception)
      }
      task.result == null -> {
        throw IOException()
      }
      else -> {
        val mutablePlannerList = requireNotNull(plannerList.value)
        mutablePlannerList.add(PlannerItem.createInstance(requireNotNull(task.result).id, firebasePlannerItem))
        plannerList.postValue(mutablePlannerList)
      }
    }
  }

  override suspend fun updatePlannerItem(id: Id, mealId: Id, date: Long) {
    withContext(Dispatchers.IO) {
      val data = mapOf(
        "mealReference" to Firebase.firestore.collection(FirebaseMeal.COLLECTION_NAME)
          .document((mealId as FirebaseId).value),
        "date" to date
      )
      val task = collection.document((id as FirebaseId).value).update(data).apply { Tasks.await(this) }
      if (task.exception != null) throw IOException(task.exception)
      else {
        getPlannerItemForId(id)?.apply {
          this.mealId = mealId
          this.date = date
          plannerList.postValue(plannerList.value)
        }
      }
    }
  }

  override suspend fun deletePlannerItem(id: Id) = withContext(Dispatchers.IO) {
    requireNotNull(plannerList.value).remove(getPlannerItemForId(id))
    val task = collection.document((id as FirebaseId).value).delete().apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception)
    else plannerList.postValue(plannerList.value)
  }
}