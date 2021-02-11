package de.usedup.android.datamodel.api.objects

import de.usedup.android.datamodel.api.exceptions.DataIntegrityException
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import de.usedup.android.datamodel.firebase.objects.FirebasePlannerItem

data class PlannerItem(val id: Id, var date: Long, var mealId: Id) {
  companion object {
    fun createInstance(id: String, firebaseObject: FirebasePlannerItem): PlannerItem {
      val date = firebaseObject.date ?: throw DataIntegrityException()
      val mealId = FirebaseId(firebaseObject.mealReference?.id ?: throw DataIntegrityException())
      return PlannerItem(FirebaseId(id), date, mealId)
    }
  }
}