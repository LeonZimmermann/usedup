package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.exceptions.DataIntegrityException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseId
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebasePlannerItem

data class PlannerItem(val id: Id, var date: Long, var mealId: Id) {
  companion object {
    fun createInstance(id: String, firebaseObject: FirebasePlannerItem): PlannerItem {
      val date = firebaseObject.date ?: throw DataIntegrityException()
      val mealId = FirebaseId(firebaseObject.mealReference?.id ?: throw DataIntegrityException())
      return PlannerItem(FirebaseId(id), date, mealId)
    }
  }
}