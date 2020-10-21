package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects

import java.time.LocalDate

data class PlannerItem(val date: LocalDate, val meal: Meal) {
  companion object {
    /*
    fun createInstance(id: String, firebaseObject: FirebasePlannerItem): PlannerItem {}
     */
  }
}