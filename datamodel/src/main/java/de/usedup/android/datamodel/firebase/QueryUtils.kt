package de.usedup.android.datamodel.firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.usedup.android.datamodel.firebase.objects.FirebaseHousehold
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import de.usedup.android.datamodel.firebase.repositories.FirebaseHouseholdRepository

fun getHouseholdReference(): DocumentReference {
  val householdId = (FirebaseHouseholdRepository.getHousehold().blockingGet().id as FirebaseId).value
  return Firebase.firestore.collection(FirebaseHousehold.COLLECTION_NAME).document(householdId)
}

fun Query.filterForHousehold(): Query {
  return whereEqualTo("householdReference", getHouseholdReference())
}