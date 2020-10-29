package com.hotmail.leon.zimmermann.homeassistant.app.planner.selection

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.hotmail.leon.zimmermann.homeassistant.app.toLongValue
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Meal
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.PlannerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate

class PlannerItemSelectionViewModel @ViewModelInject constructor(
  mealRepository: MealRepository,
  private val plannerRepository: PlannerRepository
) : ViewModel(), PlannerItemSelectionAdapter.Callback {

  val mealList: MutableLiveData<MutableList<Meal>> = mealRepository.meals
  lateinit var date: LocalDate
  val errorMessage = MutableLiveData<String>()

  override fun onMealSelected(view: View, meal: Meal) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        plannerRepository.addPlannerItem(meal.id, date.toLongValue())
        viewModelScope.launch(Dispatchers.Main) { Navigation.findNavController(view).navigateUp() }
      } catch (e: IOException) {
        errorMessage.postValue("A network error occurred")
      }
    }
  }
}