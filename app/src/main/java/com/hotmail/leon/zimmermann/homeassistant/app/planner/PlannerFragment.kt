package com.hotmail.leon.zimmermann.homeassistant.app.planner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.PlannerFragmentBinding
import kotlinx.android.synthetic.main.planner_fragment.*

class PlannerFragment : Fragment() {

  companion object {
    fun newInstance() = PlannerFragment()
  }

  private val viewModel: PlannerViewModel by viewModels()
  private lateinit var binding: PlannerFragmentBinding

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
    val adapter = PlannerRecyclerAdapter(requireContext(), viewModel::onPlannerItemClicked)
    recycler_view.adapter = adapter
    recycler_view.layoutManager = LinearLayoutManager(requireContext())
    viewModel.plannerItems.observe(viewLifecycleOwner, Observer { plannerItems ->
      adapter.setPlannerItems(plannerItems)
    })
  }
}