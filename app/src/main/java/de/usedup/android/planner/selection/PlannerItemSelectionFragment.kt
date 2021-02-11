package de.usedup.android.planner.selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import de.usedup.android.R
import de.usedup.android.utils.toLocalDate
import de.usedup.android.datamodel.api.objects.Id
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.planner_item_selection_fragment.*

@AndroidEntryPoint
class PlannerItemSelectionFragment : Fragment() {

  private val viewModel: PlannerItemSelectionViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.plannerItemId = arguments?.getSerializable(PLANNER_ITEM_ID) as? Id?
    viewModel.date = requireNotNull(arguments?.getLong(DATE)).toLocalDate()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.planner_item_selection_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initErrorMessageSnackbar()
    initRecyclerView()
  }

  private fun initErrorMessageSnackbar() {
    viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
      Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show()
    })
  }

  private fun initRecyclerView() {
    val adapter = PlannerItemSelectionAdapter(requireContext(), viewModel)
    recycler_view.adapter = adapter
    recycler_view.layoutManager = LinearLayoutManager(requireContext())
    viewModel.mealList.observe(viewLifecycleOwner, Observer { mealList ->
      adapter.setMealList(mealList)
    })
  }

  companion object {
    const val PLANNER_ITEM_ID = "plannerItemId"
    const val DATE = "date"

    fun newInstance() = PlannerItemSelectionFragment()
  }

}