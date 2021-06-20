package de.usedup.android.datamodel.api.objects

import de.usedup.android.datamodel.api.exceptions.DataIntegrityException
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import de.usedup.android.datamodel.firebase.objects.FirebaseInvitation

data class Invitation(
  val id: Id,
  val sender: Id,
  val receiver: Id,
  val message: String?,
) {
  companion object {
    internal fun createInstance(id: String, firebaseObject: FirebaseInvitation): Invitation {
      val sender = firebaseObject.senderReference ?: throw DataIntegrityException()
      val receiver = firebaseObject.receiverReference ?: throw DataIntegrityException()
      val message = firebaseObject.message
      return Invitation(FirebaseId(id), sender, receiver, message)
    }
  }
}
