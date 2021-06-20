package de.usedup.android.invitationlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.usedup.android.R
import de.usedup.android.invitationdetails.InvitationDetailsFragment
import kotlinx.android.synthetic.main.invitation_list_fragment.*

@AndroidEntryPoint
class InvitationListFragment : Fragment() {

  private val viewModel: InvitationListViewModel by viewModels()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.invitation_list_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initRecyclerView()
    initSelectedInvitationCallback()
    initSnackbarMessage()
  }

  private fun initRecyclerView() {
    val adapter = InvitationRecyclerAdapter(requireContext(), viewModel)
    invitation_recyclerview.layoutManager = LinearLayoutManager(requireContext())
    invitation_recyclerview.adapter = adapter
    invitation_recyclerview.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    viewModel.invitations.observe(viewLifecycleOwner) { invitations ->
      adapter.initInvitations(invitations)
    }
  }

  private fun initSelectedInvitationCallback() {
    viewModel.selectedInvitation.observe(viewLifecycleOwner) { selectedInvitation ->
      selectedInvitation?.let {
        findNavController().navigate(R.id.action_invitation_list_fragment_to_invitation_details_fragment, bundleOf(
          InvitationDetailsFragment.INVITATION_ID to it.id
        ))
        viewModel.selectedInvitation.postValue(null)
      }
    }
  }

  private fun initSnackbarMessage() {
    viewModel.snackbarMessage.observe(viewLifecycleOwner) { snackbarMessage ->
      snackbarMessage?.let {
        Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        viewModel.snackbarMessage.postValue(null)
      }
    }
  }

  companion object {
    fun newInstance() = InvitationListFragment()
  }
}