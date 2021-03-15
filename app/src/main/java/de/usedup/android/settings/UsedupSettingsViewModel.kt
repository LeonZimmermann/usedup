package de.usedup.android.settings

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.activities.main.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class UsedupSettingsViewModel @Inject constructor(@ApplicationContext private val context: Context) :
  ViewModel() {

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
    val intent = Intent(context, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP }
    context.startActivity(intent)
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