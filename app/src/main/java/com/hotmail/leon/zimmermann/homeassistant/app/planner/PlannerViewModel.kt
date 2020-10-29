package com.hotmail.leon.zimmermann.homeassistant.app.planner

import android.view.View
import androidx.core.os.bundleOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.app.planner.selection.PlannerItemSelectionFragment
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.PlannerItem
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.PlannerRepository
import java.time.LocalDate

class PlannerViewModel @ViewModelInject constructor(private val mealRepository: MealRepository,
  private val plannerRepository: PlannerRepository) : ViewModel(),
  PlannerRecyclerAdapter.Callbacks {

  val plannerItems: MutableLiveData<MutableList<PlannerItem>> = plannerRepository.plan

  override fun onPreviewButtonClicked(view: View, plannerItem: PlannerItem) {
    Navigation.findNavController(view).navigate(R.id.action_planner_fragment_to_meal_details_fragment,
      bundleOf("mealId" to plannerItem.mealId))
  }

  override fun onAddButtonClicked(view: View, date: LocalDate) {
    Navigation.findNavController(view)
      .navigate(R.id.action_planner_fragment_to_planner_item_selection_fragment, bundleOf(
        PlannerItemSelectionFragment.DATE to date
      ))
  }

  override fun onChangeButtonClicked(view: View, plannerItem: PlannerItem) {
    TODO("not implemented")
  }
}