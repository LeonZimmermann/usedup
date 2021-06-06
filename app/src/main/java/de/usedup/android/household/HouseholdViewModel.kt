package de.usedup.android.household

import android.util.Patterns
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.internal.Mutable
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.datamodel.api.repositories.HouseholdRepository
import de.usedup.android.datamodel.api.repositories.UserRepository
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class HouseholdViewModel @Inject constructor(
  householdRepository: HouseholdRepository,
  private val userRepository: UserRepository,
) : ViewModel(), HouseholdMemberRecyclerAdapter.Callback, EmailInputDialog.Callback {

  val householdMembers: MutableLiveData<MutableList<HouseholdMemberRepresentation>> = MutableLiveData(mutableListOf())

  val memberPreview: MutableLiveData<HouseholdMemberRepresentation?> = MutableLiveData()

  val showEmailDialog: MutableLiveData<Boolean> = MutableLiveData(false)

  val errorMessage: MutableLiveData<String?> = MutableLiveData(null)

  init {
    householdRepository.getHousehold().onErrorComplete().subscribe { household ->
      userRepository.getUsersFlowable(household.memberIds)
        .onErrorComplete()
        .subscribeOn(Schedulers.io())
        .subscribe { user ->
          val role = if (household.adminId == user.id) HouseholdMemberRepresentation.Role.ADMIN
          else HouseholdMemberRepresentation.Role.MEMBER
          val householdMemberList = requireNotNull(householdMembers.value)
          householdMemberList.add(HouseholdMemberRepresentation(user.id, user.photoUrl, user.name, role))
          this.householdMembers.postValue(householdMemberList)
        }
    }
  }

  override fun onPreviewButtonClicked(adapter: HouseholdMemberRecyclerAdapter, index: Int, view: View,
    householdMemberRepresentation: HouseholdMemberRepresentation) {
    memberPreview.postValue(householdMemberRepresentation)
  }

  fun onAddUserButtonClicked(view: View) {
    showEmailDialog.postValue(true)
  }

  override fun onPositiveButtonClicked(email: String) {
    if (email.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      errorMessage.postValue("\"$email\" is not a valid email address")
      return
    }
    errorMessage.postValue("TODO: Implement")
  }
}