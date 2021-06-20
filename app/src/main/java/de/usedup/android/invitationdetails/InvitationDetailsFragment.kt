package de.usedup.android.invitationdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.usedup.android.R
import de.usedup.android.databinding.InvitationDetailsFragmentBinding
import de.usedup.android.datamodel.api.objects.Id

@AndroidEntryPoint
class InvitationDetailsFragment : Fragment() {

  private val viewModel: InvitationDetailsViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.apply {
      viewModel.setInvitationId(getSerializable(INVITATION_ID) as Id)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.invitation_details_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding()
    initSnackbarMessage()
  }

  private fun initDatabinding() {
    val binding = InvitationDetailsFragmentBinding.bind(requireView())
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
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
    const val INVITATION_ID = "invitationId"

    fun newInstance() = InvitationDetailsFragment()
  }
}