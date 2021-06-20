package de.usedup.android.invitationdetails

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.invitationlist.InvitationListViewModel
import de.usedup.android.invitationlist.InvitationRepresentation
import javax.inject.Inject

@HiltViewModel
class InvitationDetailsViewModel @Inject constructor() : ViewModel() {

  class MockId : Id {
    override fun equals(other: Any?): Boolean = other == this
  }

  val invitation: MutableLiveData<InvitationRepresentation> by lazy {
    MutableLiveData(InvitationRepresentation(InvitationListViewModel.MockId(), null, "Peter Lustig",
      "Hallo Leon, möchtest du Teil meines Haushalts werden?"))
  }

  val snackbarMessage: MutableLiveData<String?> = MutableLiveData()

  fun initInvitation(id: Id) {
    snackbarMessage.postValue("TODO: Implement initInvitation")
  }

  fun onAcceptClicked(view: View) {
    snackbarMessage.postValue("TODO: Implement onAcceptClicked")
  }

  fun onDeclineClicked(view: View) {
    snackbarMessage.postValue("TODO: Implement onDeclineClicked")
  }
}