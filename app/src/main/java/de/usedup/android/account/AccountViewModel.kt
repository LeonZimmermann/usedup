package de.usedup.android.account

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.R
import de.usedup.android.datamodel.api.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
  private val userRepository: UserRepository,
) : ViewModel() {

  private val authentication = FirebaseAuth.getInstance()

  val errorMessage: MutableLiveData<String> = MutableLiveData()
  val userImage: MutableLiveData<String?> = MutableLiveData()
  val userName: MutableLiveData<String> = MutableLiveData()
  val userEmail: MutableLiveData<String> = MutableLiveData()

  val backToMainActivity: MutableLiveData<Boolean> = MutableLiveData()
  val navigateUp: MutableLiveData<Boolean> = MutableLiveData()

  init {
    viewModelScope.launch(Dispatchers.IO) {
      val user = userRepository.getCurrentUser()
      userImage.postValue(user.photoUrl)
      userName.postValue(user.name)
      userEmail.postValue(user.email)
    }
  }

  fun onLogoutClicked() {
    authentication.signOut()
    backToMainActivity.postValue(true)
  }

  companion object {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
      if (!url.isNullOrBlank()) {
        Glide.with(view.context)
          .load(url)
          .circleCrop()
          .into(view)
      }
    }
  }
}