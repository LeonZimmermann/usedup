package com.hotmail.leon.zimmermann.homeassistant.app.planner

import android.view.View
import androidx.core.os.bundleOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.app.planner.selection.PlannerItemSelectionFragment
import com.hotmail.leon.zimmermann.homeassistant.app.toLongValue
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.PlannerItem
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.PlannerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class PlannerViewModel @ViewModelInject constructor(private val plannerRepository: PlannerRepository) : ViewModel(),
  PlannerRecyclerAdapter.Callbacks {

  val plannerItems: MutableLiveData<MutableList<PlannerItem>> = plannerRepository.plan

  override fun onPreviewButtonClicked(view: View, plannerItem: PlannerItem) {
    Navigation.findNavController(view).navigate(R.id.action_planner_fragment_to_meal_details_fragment,
      bundleOf("mealId" to plannerItem.mealId))
  }

  override fun onAddButtonClicked(view: View, date: LocalDate) {
    Navigation.findNavController(view)
      .navigate(R.id.action_planner_fragment_to_planner_item_selection_fragment, bundleOf(
        PlannerItemSelectionFragment.DATE to date.toLongValue()
      ))
  }

  override fun onChangeButtonClicked(view: View, plannerItem: PlannerItem) {
    Navigation.findNavController(view)
      .navigate(R.id.action_planner_fragment_to_planner_item_selection_fragment, bundleOf(
        PlannerItemSelectionFragment.PLANNER_ITEM_ID to plannerItem.id,
        PlannerItemSelectionFragment.DATE to plannerItem.date
      ))
  }

  override fun onDeleteButtonClicked(view: View, plannerItem: PlannerItem) {
    viewModelScope.launch(Dispatchers.IO) {
      plannerRepository.deletePlannerItem(plannerItem.id)
    }
  }
}