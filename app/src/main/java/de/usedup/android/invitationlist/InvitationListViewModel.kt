package de.usedup.android.invitationlist

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.datamodel.api.objects.Id
import javax.inject.Inject

@HiltViewModel
class InvitationListViewModel @Inject constructor() : ViewModel(), InvitationRecyclerAdapter.Callback {

  class MockId : Id {
    override fun equals(other: Any?): Boolean = other == this
  }

  val invitations: MutableLiveData<List<InvitationRepresentation>> by lazy {
    MutableLiveData(mutableListOf(
      InvitationRepresentation(MockId(), null, "Leon Zimmermann", null),
      InvitationRepresentation(MockId(), null, "Leon Zimmermann", null),
      InvitationRepresentation(MockId(), null, "Leon Zimmermann", null),
    ))
  }

  val selectedInvitation: MutableLiveData<InvitationRepresentation?> = MutableLiveData()

  val snackbarMessage: MutableLiveData<String?> = MutableLiveData()

  override fun onPreviewButtonClicked(adapter: InvitationRecyclerAdapter, index: Int, view: View,
    invitationRepresentation: InvitationRepresentation) {
    selectedInvitation.postValue(invitationRepresentation)
  }
}