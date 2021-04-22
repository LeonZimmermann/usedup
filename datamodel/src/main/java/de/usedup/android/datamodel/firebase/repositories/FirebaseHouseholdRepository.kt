package de.usedup.android.datamodel.firebase.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.usedup.android.datamodel.api.objects.Household
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.repositories.HouseholdRepository
import de.usedup.android.datamodel.firebase.objects.FirebaseHousehold
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import de.usedup.android.datamodel.firebase.objects.FirebaseUser
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

object FirebaseHouseholdRepository : HouseholdRepository {

  private const val TAG = "HouseholdRepository"

  private val collection = Firebase.firestore.collection(FirebaseHousehold.COLLECTION_NAME)

  private var household: MutableLiveData<Household> = MutableLiveData()

  override fun createHousehold(adminId: Id) {
    val adminReference = Firebase.firestore.collection(FirebaseUser.COLLECTION_NAME)
      .document((adminId as FirebaseId).value)
    val firebaseHousehold = FirebaseHousehold(adminReference, listOf(adminReference))
    collection.add(firebaseHousehold).addOnSuccessListener {
      household.postValue(Household.createInstance(it.id, firebaseHousehold))
    }.addOnFailureListener {
      Log.e(TAG, it.message ?: "An error occurred while creating household")
      throw IOException(it)
    }
  }

  override fun getHouseholdLiveData(coroutineScope: CoroutineScope): LiveData<Household> {
    if (household.value == null) {
      coroutineScope.launch(Dispatchers.IO) {
        val userReference = FirebaseUserRepository.getDocumentReferenceToCurrentUser()
        var fetchedHousehold = Tasks.await(collection.whereArrayContains("memberReferences", userReference).get())
          .map { Household.createInstance(it.id, it.toObject()) }
          .firstOrNull()
        if (fetchedHousehold == null) {
          createHousehold(FirebaseId(FirebaseUserRepository.getDocumentReferenceToCurrentUser().id))
          fetchedHousehold = Tasks.await(collection.whereArrayContains("memberReferences", userReference).get())
            .map { Household.createInstance(it.id, it.toObject()) }
            .first()
        }
        household.postValue(fetchedHousehold)
      }
    }
    return household
  }

  override fun getHousehold(): Single<Household> {
    return Single.fromCallable {
      if (household.value == null) {
        val userReference = Firebase.firestore.collection(FirebaseUser.COLLECTION_NAME)
          .document(FirebaseUserRepository.getDocumentReferenceToCurrentUser().id)
        var fetchedHousehold = Tasks.await(collection.whereArrayContains("memberReferences", userReference).get())
          .map { Household.createInstance(it.id, it.toObject()) }
          .firstOrNull()
        if (fetchedHousehold == null) {
          createHousehold(FirebaseId(FirebaseUserRepository.getDocumentReferenceToCurrentUser().id))
          fetchedHousehold = Tasks.await(collection.whereArrayContains("memberReferences", userReference).get())
            .map { Household.createInstance(it.id, it.toObject()) }
            .first()
        }
        this@FirebaseHouseholdRepository.household.postValue(fetchedHousehold)
        fetchedHousehold
      } else {
        requireNotNull(household.value)
      }
    }
  }

  private suspend fun initHousehold() {
    withContext(Dispatchers.IO) {
      val userReference = FirebaseUserRepository.getDocumentReferenceToCurrentUser()
      var household = Tasks.await(collection.whereArrayContains("memberReferences", userReference).get())
        .map { Household.createInstance(it.id, it.toObject()) }
        .firstOrNull()
      if (household == null) {
        createHousehold(FirebaseId(FirebaseUserRepository.getDocumentReferenceToCurrentUser().id))
        household = Tasks.await(collection.whereArrayContains("memberReferences", userReference).get())
          .map { Household.createInstance(it.id, it.toObject()) }
          .first()
      }
      this@FirebaseHouseholdRepository.household.postValue(household)
    }
  }

  override suspend fun addMember(memberId: Id) {
    withContext(Dispatchers.IO) {
      if (household.value == null) {
        initHousehold()
      } else {
        val household = requireNotNull(household.value)
        val householdMembers = household.memberIds.toMutableSet()
        householdMembers.add(memberId)
        val memberReferences = householdMembers.map {
          Firebase.firestore.collection(FirebaseUser.COLLECTION_NAME).document((it as FirebaseId).value)
        }.toSet()
        val updatedData = mapOf("memberReferences" to memberReferences)
        val task =
          collection.document((household.id as FirebaseId).value).update(updatedData).apply { Tasks.await(this) }
        if (task.exception != null) throw IOException(task.exception)
      }
    }
  }

  override suspend fun removeMember(memberId: Id) {
    withContext(Dispatchers.IO) {
      if (household.value == null) {
        initHousehold()
      } else {
        val household = requireNotNull(household.value)
        val householdMembers = household.memberIds.toMutableSet()
        householdMembers.remove(memberId)
        val memberReferences = householdMembers.map {
          Firebase.firestore.collection(FirebaseUser.COLLECTION_NAME).document((it as FirebaseId).value)
        }.toSet()
        val updatedData = mapOf("memberReferences" to memberReferences)
        val task =
          collection.document((household.id as FirebaseId).value).update(updatedData).apply { Tasks.await(this) }
        if (task.exception != null) throw IOException(task.exception)
      }
    }
  }
}