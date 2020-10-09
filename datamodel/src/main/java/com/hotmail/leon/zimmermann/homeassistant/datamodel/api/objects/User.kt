package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.exceptions.DataIntegrityException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseId
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseUser

data class User(
  val id: Id,
  var name: String,
  var email: String
) {
  companion object {
    internal fun createInstance(firebaseObject: FirebaseUser): User {
      val id = firebaseObject.id ?: throw DataIntegrityException()
      val name = firebaseObject.name ?: throw DataIntegrityException()
      val email = firebaseObject.email ?: throw DataIntegrityException()
      return User(FirebaseId(id), name, email)
    }
  }
}