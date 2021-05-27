package de.usedup.android.household

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import de.usedup.android.R
import de.usedup.android.databinding.HouseholdFragmentBinding
import kotlinx.android.synthetic.main.household_fragment.*

class HouseholdFragment : Fragment() {

  private val viewModel: HouseholdViewModel by viewModels()

  private lateinit var householdMemberAdapter: HouseholdMemberRecyclerAdapter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.household_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding()
    initHouseholdMemberAdapter()
  }

  private fun initDatabinding() {
    val binding = HouseholdFragmentBinding.bind(requireView())
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
  }

  private fun initHouseholdMemberAdapter() {
    householdMemberAdapter = HouseholdMemberRecyclerAdapter(requireContext(), viewModel)
    users_recyclerview.adapter = householdMemberAdapter
    users_recyclerview.layoutManager = LinearLayoutManager(requireContext())
    viewModel.householdMembers.observe(viewLifecycleOwner) { householdMembers ->
      householdMemberAdapter.initHouseholdMembers(householdMembers)
    }
  }

  companion object {
    fun newInstance() = HouseholdFragment()
  }

}