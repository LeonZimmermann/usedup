package com.hotmail.leon.zimmermann.homeassistant.app.planner

import android.view.View
import androidx.core.os.bundleOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MealRepository
import java.time.LocalDate

class PlannerViewModel @ViewModelInject constructor(private val mealRepository: MealRepository) : ViewModel(),
  PlannerRecyclerAdapter.Callbacks {

  val plannerItems: MutableLiveData<MutableList<PlannerItem>> = MutableLiveData()

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