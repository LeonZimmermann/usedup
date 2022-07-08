package de.usedup.android.account

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
  private val userRepository: UserRepository,
) : ViewModel() {

  private val authentication = FirebaseAuth.getInstance()

  val userId: MutableLiveData<Id> = MutableLiveData()
  val userImage: MutableLiveData<String?> = MutableLiveData()
  val userName: MutableLiveData<String> = MutableLiveData()
  val userEmail: MutableLiveData<String> = MutableLiveData()

  val errorMessage: MutableLiveData<String> = MutableLiveData()
  val editMode: MutableLiveData<Boolean> = MutableLiveData(false)

  val backToMainActivity: MutableLiveData<Boolean> = MutableLiveData()
  val navigateUp: MutableLiveData<Boolean> = MutableLiveData()

  init {
    synchronize()
  }

  fun synchronize() {
    viewModelScope.launch(Dispatchers.IO) {
      val user = userRepository.getCurrentUser()
      userId.postValue(user.id)
      userImage.postValue(user.photoUrl)
      userName.postValue(user.name)
      userEmail.postValue(user.email)
    }
  }

  fun onLogoutClicked() {
    authentication.signOut()
    backToMainActivity.postValue(true)
  }

  fun onEditButtonClicked(view: View) {
    editMode.postValue(!requireNotNull(editMode.value))
    // TODO Add proper Validation
    if (userName.value?.isBlank() != false) {
      errorMessage.postValue("Name cannot be blank")
      return
    }
    viewModelScope.launch(Dispatchers.IO) {
      userRepository.updateUser(requireNotNull(userId.value), requireNotNull(userName.value),
        requireNotNull(userEmail.value), userImage.value)
    }
  }
}