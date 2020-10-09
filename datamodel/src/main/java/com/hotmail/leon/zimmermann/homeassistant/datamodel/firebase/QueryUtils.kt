package com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase

import com.google.firebase.firestore.Query
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories.FirebaseUserRepository

fun Query.filterForUser(): Query {
  val userReference = FirebaseUserRepository.getDocumentReferenceToCurrentUser()
  return whereEqualTo("userReference", userReference)
}