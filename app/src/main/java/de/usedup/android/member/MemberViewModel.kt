package de.usedup.android.member

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.repositories.HouseholdRepository
import de.usedup.android.datamodel.api.repositories.UserRepository
import de.usedup.android.household.HouseholdMemberRepresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
  private val userRepository: UserRepository,
  private val householdRepository: HouseholdRepository,
) : ViewModel() {

  val member: MutableLiveData<HouseholdMemberRepresentation> = MutableLiveData()

  fun setMember(memberId: Id) = viewModelScope.launch(Dispatchers.IO) {
    userRepository.getUser(memberId)
      .zipWith(householdRepository.getHousehold()) { user, household -> Pair(user, household) }
      .subscribe { (user, household) ->
        val role = if (household.adminId == user.id) HouseholdMemberRepresentation.Role.ADMIN
        else HouseholdMemberRepresentation.Role.MEMBER
        member.postValue(HouseholdMemberRepresentation(user.id, user.photoUrl, user.name, role))
      }
  }

  fun onAddPermissionClicked(view: View) {
  }

  fun onRemovePermissionClicked(view: View) {
  }

  fun onRemoveClicked(view: View) {

  }
}