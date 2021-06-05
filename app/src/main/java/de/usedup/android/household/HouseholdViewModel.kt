package de.usedup.android.household

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HouseholdViewModel @Inject constructor() : ViewModel(), HouseholdMemberRecyclerAdapter.Callback {

  val householdMembers: MutableLiveData<Collection<HouseholdMemberRepresentation>> by lazy {
    MutableLiveData(listOf(
      HouseholdMemberRepresentation("...", "Leon Zimmermann", HouseholdMemberRepresentation.Role.ADMIN),
      HouseholdMemberRepresentation("...", "Member 1", HouseholdMemberRepresentation.Role.ADMIN),
      HouseholdMemberRepresentation("...", "Member 2", HouseholdMemberRepresentation.Role.ADMIN),
      HouseholdMemberRepresentation("...", "Member 3", HouseholdMemberRepresentation.Role.ADMIN),
    ))
  }

  val memberPreview: MutableLiveData<HouseholdMemberRepresentation?> = MutableLiveData()

  override fun onPreviewButtonClicked(adapter: HouseholdMemberRecyclerAdapter, index: Int, view: View,
    householdMemberRepresentation: HouseholdMemberRepresentation) {
    memberPreview.postValue(householdMemberRepresentation)
  }

  fun onAddUserButtonClicked(view: View) {
    Snackbar.make(view, "TODO: adding user requested", Snackbar.LENGTH_LONG).show()
  }
}