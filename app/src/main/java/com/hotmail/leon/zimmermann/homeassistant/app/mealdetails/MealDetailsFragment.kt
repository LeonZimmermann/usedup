package com.hotmail.leon.zimmermann.homeassistant.app.mealdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.app.management.meals.MealEditorFragment
import com.hotmail.leon.zimmermann.homeassistant.databinding.MealDetailsFragmentBinding

class MealDetailsFragment : Fragment() {

  private val viewModel: MealDetailsViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requireNotNull(arguments).apply {
      val mealId = getSerializable(MealEditorFragment.MEAL_ID) as? String?
      viewModel.setMealId(requireNotNull(mealId))
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.meal_details_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding(view)
  }

  private fun initDatabinding(view: View) {
    val binding = MealDetailsFragmentBinding.bind(view)
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
  }

  companion object {
    fun newInstance() = MealDetailsFragment()
  }

}
