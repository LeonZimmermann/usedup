package de.usedup.android.activities.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.activities.app.AppActivity
import de.usedup.android.activities.loading.LoadingActivity
import de.usedup.android.activities.login.LoginFlowActivity
import de.usedup.android.datamodel.api.repositories.UserRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

  val activityToStart: MutableLiveData<Class<*>> = MutableLiveData()

  fun navigateActivity() {
    if (FirebaseAuth.getInstance().currentUser != null) {
      if (userRepository.isInitialized()) {
        activityToStart.postValue(AppActivity::class.java)
      } else {
        activityToStart.postValue(LoadingActivity::class.java)
      }
    } else {
      activityToStart.postValue(LoginFlowActivity::class.java)
    }

  }
}