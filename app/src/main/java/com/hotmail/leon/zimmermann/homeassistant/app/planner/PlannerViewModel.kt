package com.hotmail.leon.zimmermann.homeassistant.app.planner

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MealRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class PlannerViewModel : ViewModel() {

  val plannerItems: MutableLiveData<List<PlannerItem>> = MutableLiveData()

  init {
    viewModelScope.launch {
      val testList = mutableListOf<PlannerItem>()
      val testMeal = MealRepository.getMealForId("qZKQBumS3lxN3FSIJX5c")
      val testDate = LocalDate.now()
      repeat(5) { index ->
        testList.add(PlannerItem(testDate.plusDays(index.toLong()), testMeal))
      }
      plannerItems.postValue(testList)
    }
  }

  fun onPlannerItemClicked(plannerItem: PlannerItem) {
    TODO("Implement")
  }

  fun onEditButtonPressed(view: View) {
    TODO("Implement")
  }
}