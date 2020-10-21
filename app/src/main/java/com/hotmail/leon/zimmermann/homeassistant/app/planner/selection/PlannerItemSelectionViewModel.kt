package com.hotmail.leon.zimmermann.homeassistant.app.planner.selection

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Meal
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MealRepository
import java.time.LocalDate

class PlannerItemSelectionViewModel @ViewModelInject constructor(
  mealRepository: MealRepository
) : ViewModel(), PlannerItemSelectionAdapter.Callback {
  val mealList: MutableLiveData<MutableList<Meal>> = mealRepository.meals
  lateinit var date: LocalDate

  override fun onMealSelected(meal: Meal) {
    Log.d("MEAL_SELECTED", "Date: $date")
    Log.d("MEAL_SELECTED", "Meal: $meal")
    TODO("Implement")
  }
}