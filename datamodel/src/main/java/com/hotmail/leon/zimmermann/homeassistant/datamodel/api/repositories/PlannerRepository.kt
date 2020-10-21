package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories

import androidx.lifecycle.MutableLiveData
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Meal
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.PlannerItem
import java.time.LocalDate

interface PlannerRepository {
  val plan: MutableLiveData<MutableList<PlannerItem>>
  suspend fun init()
  suspend fun addPlannerItem(meal: Meal, date: LocalDate)
  suspend fun updatePlannerItem(id: Id, meal: Meal, date: LocalDate)
  suspend fun deletePlannerItem(id: Id)
}