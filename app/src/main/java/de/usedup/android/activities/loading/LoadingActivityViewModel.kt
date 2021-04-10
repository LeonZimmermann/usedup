package de.usedup.android.activities.loading

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.usedup.android.activities.app.AppActivity
import de.usedup.android.datamodel.api.repositories.CategoryRepository
import de.usedup.android.datamodel.api.repositories.MeasureRepository
import de.usedup.android.datamodel.api.repositories.UserRepository
import de.usedup.android.notifications.UsedupNotificationChannelManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingActivityViewModel @Inject constructor(
  @ApplicationContext private val context: Context,
  private val userRepository: UserRepository,
  private val categoryRepository: CategoryRepository,
  private val measureRepository: MeasureRepository,
) : ViewModel() {

  val activityToStart: MutableLiveData<Class<*>> = MutableLiveData()

  fun initializeApp() = viewModelScope.launch(Dispatchers.IO) {
    UsedupNotificationChannelManager.getInstance(context).createNotificationChannels()
    userRepository.init()
    categoryRepository.init()
    measureRepository.init()
    activityToStart.postValue(AppActivity::class.java)
  }
}