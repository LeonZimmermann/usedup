package de.usedup.android.datamodel.firebase

import com.google.firebase.firestore.Query
import de.usedup.android.datamodel.firebase.repositories.FirebaseUserRepository

fun Query.filterForUser(): Query {
  val userReference = FirebaseUserRepository.getDocumentReferenceToCurrentUser()
  return whereEqualTo("userReference", userReference)
}