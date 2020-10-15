package com.hotmail.leon.zimmermann.homeassistant.app.activities

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.app.notifications.HomeAssistantNotificationChannelManager
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel @ViewModelInject constructor(
  @ApplicationContext private val context: Context,
  private val userRepository: UserRepository,
  private val productRepository: ProductRepository,
  private val templateRepository: TemplateRepository,
  private val mealRepository: MealRepository,
  private val categoryRepository: CategoryRepository,
  private val measureRepository: MeasureRepository
) : ViewModel() {

  val activityToStart: MutableLiveData<Class<*>> = MutableLiveData()

  fun initAppAndStartActivity() = viewModelScope.launch(Dispatchers.IO) {
    HomeAssistantNotificationChannelManager.getInstance(context).createNotificationChannels()
    userRepository.init()
    productRepository.init()
    templateRepository.init()
    mealRepository.init()
    categoryRepository.init()
    measureRepository.init()
    activityToStart.postValue(AppActivity::class.java)
  }
}