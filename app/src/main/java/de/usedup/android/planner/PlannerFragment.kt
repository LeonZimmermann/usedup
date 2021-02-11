package de.usedup.android.planner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import de.usedup.android.R
import de.usedup.android.databinding.PlannerFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.planner_fragment.*

@AndroidEntryPoint
class PlannerFragment : Fragment() {

  companion object {
    fun newInstance() = PlannerFragment()
  }

  private val viewModel: PlannerViewModel by viewModels()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.planner_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding(view)
    initRecyclerView()
  }

  private fun initDatabinding(view: View) {
    val binding = PlannerFragmentBinding.bind(view)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
  }

  private fun initRecyclerView() {
    val adapter = PlannerRecyclerAdapter(requireContext(), viewModel.viewModelScope, viewModel)
    recycler_view.adapter = adapter
    recycler_view.layoutManager = LinearLayoutManager(requireContext())
    viewModel.plannerItems.observe(viewLifecycleOwner, Observer { plannerItems ->
      adapter.setPlannerItems(plannerItems)
    })
  }
}