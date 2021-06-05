package de.usedup.android.member

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.household.HouseholdMemberRepresentation
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor() : ViewModel() {

  val member: LiveData<HouseholdMemberRepresentation> by lazy {
    MutableLiveData(HouseholdMemberRepresentation(
      "",
      "Leon Zimmermann",
      HouseholdMemberRepresentation.Role.ADMIN
    ))
  }

  fun onAddPermissionClicked(view: View) {
  }

  fun onRemovePermissionClicked(view: View) {
  }

  fun onRemoveClicked(view: View) {

  }
}