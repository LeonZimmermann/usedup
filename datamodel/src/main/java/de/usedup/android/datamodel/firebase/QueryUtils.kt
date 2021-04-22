package de.usedup.android.datamodel.firebase

import com.google.firebase.firestore.Query
import de.usedup.android.datamodel.firebase.objects.FirebaseHousehold
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import de.usedup.android.datamodel.firebase.repositories.FirebaseHouseholdRepository
import de.usedup.android.datamodel.firebase.repositories.FirebaseUserRepository

fun Query.filterForHousehold(): Query {
  val householdId = (FirebaseHouseholdRepository.getHousehold().blockingGet().id as FirebaseId).value
  val householdReference = firestore.collection(FirebaseHousehold.COLLECTION_NAME).document(householdId)
  return whereEqualTo("householdReference", householdReference)
}