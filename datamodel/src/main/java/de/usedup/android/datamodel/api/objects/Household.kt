package de.usedup.android.datamodel.api.objects

import de.usedup.android.datamodel.api.exceptions.DataIntegrityException
import de.usedup.android.datamodel.firebase.objects.FirebaseHousehold
import de.usedup.android.datamodel.firebase.objects.FirebaseId

data class Household(
  val id: Id,
  val adminId: Id,
  val memberIds: Set<Id>,
) {
  companion object {
    internal fun createInstance(id: String, firebaseObject: FirebaseHousehold): Household {
      val adminId = FirebaseId(firebaseObject.adminReference?.id ?: throw DataIntegrityException())
      val memberIds = firebaseObject.memberReferences
        ?.map { FirebaseId(it.id) }
        ?.toSet()
        ?: throw DataIntegrityException()
      return Household(FirebaseId(id), adminId, memberIds)
    }
  }
}