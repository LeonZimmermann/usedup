package com.hotmail.leon.zimmermann.homeassistant.app.planner

import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MealRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class PlannerViewModel : ViewModel(), PlannerRecyclerAdapter.Callbacks {

  val plannerItems: MutableLiveData<MutableList<PlannerItem>> = MutableLiveData()

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

  override fun onPreviewButtonClicked(view: View, plannerItem: PlannerItem) {
    Navigation.findNavController(view).navigate(R.id.action_planner_fragment_to_meal_details_fragment,
      bundleOf("mealId" to plannerItem.meal.id))
  }

  override fun onAddButtonClicked(view: View, date: LocalDate) {
    TODO("not implemented")
  }

  override fun onChangeButtonClicked(view: View, plannerItem: PlannerItem) {
    TODO("not implemented")
  }
}