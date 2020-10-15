package com.hotmail.leon.zimmermann.homeassistant.app.settings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class HomeAssistantSettingsViewModel : ViewModel() {

  private val authentication = FirebaseAuth.getInstance()

  val errorMessage: MutableLiveData<String> = MutableLiveData()
  val userImage: MutableLiveData<String> = MutableLiveData()
  val userName: MutableLiveData<String> = MutableLiveData()
  val userEmail: MutableLiveData<String> = MutableLiveData()

  init {
    try {
      val user = requireNotNull(authentication.currentUser)
      userImage.postValue(user.photoUrl?.toString())
      userName.postValue(user.displayName)
      userEmail.postValue(user.email)
    } catch (e: IllegalArgumentException) {
      errorMessage.postValue("Cannot access account information")
      // TODO Navigate up
    }
  }

  fun onLogoutClicked() {
    authentication.signOut()
    // TODO Navigate out of app
  }

  companion object {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
      url?.let {
        Glide.with(view.context)
          .load(it)
          .circleCrop()
          .into(view)
      }
    }
  }
}