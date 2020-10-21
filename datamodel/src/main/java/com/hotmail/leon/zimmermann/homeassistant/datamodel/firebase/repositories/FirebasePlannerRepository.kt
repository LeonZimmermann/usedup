package com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories

import androidx.lifecycle.MutableLiveData
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Meal
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.PlannerItem
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.PlannerRepository
import java.time.LocalDate

object FirebasePlannerRepository: PlannerRepository {

  override val plan: MutableLiveData<MutableList<PlannerItem>> = MutableLiveData()

  override suspend fun init() {
    TODO("not implemented")
  }

  override suspend fun addPlannerItem(meal: Meal, date: LocalDate) {
    TODO("not implemented")
  }

  override suspend fun updatePlannerItem(id: Id, meal: Meal, date: LocalDate) {
    TODO("not implemented")
  }

  override suspend fun deletePlannerItem(id: Id) {
    TODO("not implemented")
  }
}