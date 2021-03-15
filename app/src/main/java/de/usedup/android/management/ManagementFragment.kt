package de.usedup.android.management

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import de.usedup.android.R
import de.usedup.android.databinding.ManagementFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.management_fragment.*

@AndroidEntryPoint
class ManagementFragment : Fragment() {

  private val viewModel: ManagementViewModel by viewModels()


  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.management_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding()
    viewModel.initAdapter(recycler_view)
    initTabLayout()
    initAdapter()
    initRecyclerView()
  }

  private fun initDatabinding() {
    val binding = ManagementFragmentBinding.bind(requireView())
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
  }

  private fun initTabLayout() {
    tab_layout.addOnTabSelectedListener(viewModel)
    viewModel.mode.observe(viewLifecycleOwner, { mode ->
      val tabPosition = when (mode) {
        ManagementViewModel.Mode.PRODUCT -> 0
        ManagementViewModel.Mode.TEMPLATE -> 1
        ManagementViewModel.Mode.MEAL -> 2
        else -> throw RuntimeException()
      }
      tab_layout.selectTab(tab_layout.getTabAt(tabPosition))
    })
  }

  private fun initAdapter() {
    viewModel.products.observe(viewLifecycleOwner, { products ->
      viewModel.adapter.products = products
    })
    viewModel.templates.observe(viewLifecycleOwner, { templates ->
      viewModel.adapter.templates = templates
    })
    viewModel.meals.observe(viewLifecycleOwner, { meals ->
      viewModel.adapter.meals = meals
    })
    viewModel.mode.observe(viewLifecycleOwner, { mode ->
      viewModel.adapter.mode = mode
    })
  }

  private fun initRecyclerView() {
    recycler_view.adapter = viewModel.adapter
    recycler_view.layoutManager = LinearLayoutManager(requireContext())
  }

  companion object {
    fun newInstance() = ManagementFragment()
  }
}