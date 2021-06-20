package de.usedup.android.invitationlist

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.datamodel.api.objects.Invitation
import de.usedup.android.datamodel.api.repositories.InvitationRepository
import de.usedup.android.datamodel.api.repositories.UserRepository
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationListViewModel @Inject constructor(
  private val invitationRepository: InvitationRepository,
  private val userRepository: UserRepository,
) : ViewModel(), InvitationRecyclerAdapter.Callback {

  val invitations: MutableLiveData<MutableList<InvitationRepresentation>> = MutableLiveData(mutableListOf())

  init {
    viewModelScope.launch(Dispatchers.IO) {
      invitationRepository.getInvitationsForCurrentUserFlowable()
        .onErrorComplete()
        .subscribe { addInvitationToList(it) }
    }
  }

  private fun addInvitationToList(invitation: Invitation) = viewModelScope.launch(Dispatchers.IO) {
    val listInLiveDataObject = requireNotNull(invitations.value)
    val sender = userRepository.getUser(invitation.sender).blockingGet()
    listInLiveDataObject.add(InvitationRepresentation(invitation.id, sender.photoUrl, sender.name, invitation.message))
    invitations.postValue(listInLiveDataObject)
  }

  val selectedInvitation: MutableLiveData<InvitationRepresentation?> = MutableLiveData()

  val snackbarMessage: MutableLiveData<String?> = MutableLiveData()

  override fun onPreviewButtonClicked(adapter: InvitationRecyclerAdapter, index: Int, view: View,
    invitationRepresentation: InvitationRepresentation) {
    selectedInvitation.postValue(invitationRepresentation)
  }
}