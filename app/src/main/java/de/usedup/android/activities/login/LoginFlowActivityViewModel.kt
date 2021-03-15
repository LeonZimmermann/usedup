package de.usedup.android.activities.login

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.usedup.android.activities.AppActivity
import de.usedup.android.datamodel.api.repositories.*
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import de.usedup.android.notifications.UsedupNotificationChannelManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginFlowActivityViewModel @Inject constructor(
  @ApplicationContext private val context: Context,
  private val userRepository: UserRepository,
  private val productRepository: ProductRepository,
  private val templateRepository: TemplateRepository,
  private val mealRepository: MealRepository,
  private val categoryRepository: CategoryRepository,
  private val measureRepository: MeasureRepository,
  private val plannerRepository: PlannerRepository
) : ViewModel() {

  val activityToStart: MutableLiveData<Class<*>> = MutableLiveData()

  fun initAppAndStartActivity() = viewModelScope.launch(Dispatchers.IO) {
    UsedupNotificationChannelManager.getInstance(context).createNotificationChannels()
    userRepository.init()
    productRepository.init()
    templateRepository.init()
    mealRepository.init()
    categoryRepository.init()
    measureRepository.init()
    plannerRepository.init()
    activityToStart.postValue(AppActivity::class.java)
  }
}