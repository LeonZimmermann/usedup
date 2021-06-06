package de.usedup.android.household

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.usedup.android.R
import de.usedup.android.databinding.HouseholdFragmentBinding
import de.usedup.android.member.MemberFragment
import kotlinx.android.synthetic.main.household_fragment.*

@AndroidEntryPoint
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
    initMemberPreviewCallback()
    initShowEmailDialogCallback()
    initErrorMessageCallback()
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

  private fun initMemberPreviewCallback() {
    viewModel.memberPreview.observe(viewLifecycleOwner) { memberPreview ->
      if (memberPreview != null) {
        findNavController().navigate(R.id.action_household_fragment_to_member_fragment, bundleOf(
          MemberFragment.MEMBER_ID to memberPreview.id
        ))
        viewModel.memberPreview.postValue(null)
      }
    }
  }

  private fun initShowEmailDialogCallback() {
    viewModel.showEmailDialog.observe(viewLifecycleOwner) { showEmailDialog ->
      if (showEmailDialog) {
        openEmailInputDialog()
        viewModel.showEmailDialog.postValue(false)
      }
    }
  }

  private fun openEmailInputDialog() {
    EmailInputDialog.newInstance().apply {
      callback = viewModel
    }.show(parentFragmentManager, "EmailInputDialog")
  }

  private fun initErrorMessageCallback() {
    viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
      if (!errorMessage.isNullOrBlank()) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show()
        viewModel.errorMessage.postValue(null)
      }
    }
  }

  companion object {
    fun newInstance() = HouseholdFragment()
  }

}