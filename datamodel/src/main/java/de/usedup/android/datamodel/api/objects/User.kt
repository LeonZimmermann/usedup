package de.usedup.android.datamodel.api.objects

import de.usedup.android.datamodel.api.exceptions.DataIntegrityException
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import de.usedup.android.datamodel.firebase.objects.FirebaseUser

data class User(
  val id: Id,
  var name: String,
  var email: String,
  var photoUrl: String?,
) {
  companion object {
    internal fun createInstance(firebaseObject: FirebaseUser): User {
      val id = firebaseObject.id ?: throw DataIntegrityException()
      val name = firebaseObject.name ?: throw DataIntegrityException()
      val email = firebaseObject.email ?: throw DataIntegrityException()
      val photoUrl = firebaseObject.photoUrl
      return User(FirebaseId(id), name, email, photoUrl)
    }
  }
}