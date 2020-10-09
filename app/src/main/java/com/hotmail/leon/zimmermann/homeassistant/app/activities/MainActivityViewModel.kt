package com.hotmail.leon.zimmermann.homeassistant.app.activities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel @ViewModelInject constructor(
  private val userRepository: UserRepository,
  private val productRepository: ProductRepository,
  private val templateRepository: TemplateRepository,
  private val mealRepository: MealRepository,
  private val categoryRepository: CategoryRepository,
  private val measureRepository: MeasureRepository
) : ViewModel() {

  val activityToStart: MutableLiveData<Class<*>> = MutableLiveData()

  fun initDataAndNavigate() = viewModelScope.launch(Dispatchers.IO) {
    userRepository.init()
    productRepository.init()
    templateRepository.init()
    mealRepository.init()
    categoryRepository.init()
    measureRepository.init()
    activityToStart.postValue(AppActivity::class.java)
  }
}