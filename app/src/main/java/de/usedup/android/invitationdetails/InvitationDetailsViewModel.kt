package de.usedup.android.invitationdetails

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Invitation
import de.usedup.android.datamodel.api.repositories.InvitationRepository
import de.usedup.android.datamodel.api.repositories.UserRepository
import de.usedup.android.invitationlist.InvitationRepresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationDetailsViewModel @Inject constructor(
  private val invitationRepository: InvitationRepository,
  private val userRepository: UserRepository,
) : ViewModel() {

  val invitation: MutableLiveData<InvitationRepresentation> = MutableLiveData()

  val snackbarMessage: MutableLiveData<String?> = MutableLiveData()

  fun setInvitationId(id: Id) {
    viewModelScope.launch(Dispatchers.IO) {
      invitationRepository.getInvitation(id)
        .doOnError { snackbarMessage.postValue("An error occured") }
        .subscribe { it -> initInvitation(it) }
    }
  }

  private fun initInvitation(invitation: Invitation) = viewModelScope.launch(Dispatchers.IO) {
    val sender = userRepository.getUser(invitation.sender).blockingGet()
    this@InvitationDetailsViewModel.invitation.postValue(
      InvitationRepresentation(invitation.id, sender.photoUrl, sender.name, invitation.message))
  }

  fun onAcceptClicked(view: View) = viewModelScope.launch(Dispatchers.IO) {
    // invitationRepository.deleteInvitation(requireNotNull(invitation.value).id)
    snackbarMessage.postValue("TODO: Implement onAcceptClicked")
  }

  fun onDeclineClicked(view: View) = viewModelScope.launch(Dispatchers.IO) {
    // invitationRepository.deleteInvitation(requireNotNull(invitation.value).id)
    snackbarMessage.postValue("TODO: Implement onDeclineClicked")
  }
}