package de.usedup.android.planner

import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.R
import de.usedup.android.datamodel.api.objects.PlannerItem
import de.usedup.android.datamodel.api.repositories.PlannerRepository
import de.usedup.android.planner.selection.PlannerItemSelectionFragment
import de.usedup.android.utils.toLongValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PlannerViewModel @Inject constructor(private val plannerRepository: PlannerRepository) : ViewModel(),
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