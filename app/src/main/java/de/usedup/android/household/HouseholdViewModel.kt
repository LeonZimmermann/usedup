package de.usedup.android.household

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar

class HouseholdViewModel : ViewModel(), HouseholdMemberRecyclerAdapter.Callback {

  val householdMembers: MutableLiveData<Collection<HouseholdMemberRepresentation>> by lazy {
    MutableLiveData(listOf(
      HouseholdMemberRepresentation("...", "Leon Zimmermann", HouseholdMemberRepresentation.Role.ADMIN),
      HouseholdMemberRepresentation("...", "Member 1", HouseholdMemberRepresentation.Role.ADMIN),
      HouseholdMemberRepresentation("...", "Member 2", HouseholdMemberRepresentation.Role.ADMIN),
      HouseholdMemberRepresentation("...", "Member 3", HouseholdMemberRepresentation.Role.ADMIN),
    ))
  }

  override fun onPreviewButtonClicked(adapter: HouseholdMemberRecyclerAdapter, index: Int, view: View,
    householdMemberRepresentation: HouseholdMemberRepresentation) {
    Snackbar.make(view, "TODO: ${householdMemberRepresentation.name} preview requested", Snackbar.LENGTH_LONG).show()
  }

  fun onAddUserButtonClicked(view: View) {
    Snackbar.make(view, "TODO: adding user requested", Snackbar.LENGTH_LONG).show()
  }
}