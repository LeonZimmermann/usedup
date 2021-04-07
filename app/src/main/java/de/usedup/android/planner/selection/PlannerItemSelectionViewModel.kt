package de.usedup.android.planner.selection

import android.view.View
import androidx.lifecycle.*
import androidx.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Meal
import de.usedup.android.datamodel.api.repositories.MealRepository
import de.usedup.android.datamodel.api.repositories.PlannerRepository
import de.usedup.android.utils.toLongValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PlannerItemSelectionViewModel @Inject constructor(
  mealRepository: MealRepository,
  private val plannerRepository: PlannerRepository
) : ViewModel(), PlannerItemSelectionAdapter.Callback {

  val mealList: LiveData<Set<Meal>> = mealRepository.getAllMeals(viewModelScope)
  val mealListEmpty: LiveData<Boolean> = Transformations.map(mealList) { it.isEmpty() }
  val errorMessage = MutableLiveData<String>()

  var plannerItemId: Id? = null
  lateinit var date: LocalDate

  override fun onMealSelected(view: View, meal: Meal) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        plannerItemId?.let { plannerRepository.updatePlannerItem(it, meal.id, date.toLongValue()) }
          ?: plannerRepository.addPlannerItem(meal.id, date.toLongValue())
        viewModelScope.launch(Dispatchers.Main) { Navigation.findNavController(view).navigateUp() }
      } catch (e: IOException) {
        errorMessage.postValue("A network error occurred")
      }
    }
  }
}