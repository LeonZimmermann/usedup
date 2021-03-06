package de.usedup.android.mealdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import de.usedup.android.R
import de.usedup.android.management.meals.MealEditorFragment
import de.usedup.android.databinding.MealDetailsFragmentBinding
import de.usedup.android.datamodel.api.objects.Id
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealDetailsFragment : Fragment() {

  private val viewModel: MealDetailsViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requireNotNull(arguments).apply {
      val mealId = getSerializable(MealEditorFragment.MEAL_ID) as? Id?
      viewModel.setMealId(requireNotNull(mealId))
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.meal_details_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding(view)
    initErrorMessage()
    initNavigateUp()
  }

  private fun initDatabinding(view: View) {
    val binding = MealDetailsFragmentBinding.bind(view)
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
  }

  private fun initErrorMessage() {
    viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
      errorMessage?.let { Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show() }
    }
  }

  private fun initNavigateUp() {
    viewModel.navigateUp.observe(viewLifecycleOwner) { navigateUp ->
      if (navigateUp) {
        Navigation.findNavController(requireView()).navigateUp()
        viewModel.navigateUp.postValue(false)
      }
    }
  }

  companion object {
    fun newInstance() = MealDetailsFragment()
  }

}
