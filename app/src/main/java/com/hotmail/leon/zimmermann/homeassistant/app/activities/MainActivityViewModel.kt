package com.hotmail.leon.zimmermann.homeassistant.app.activities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.CategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MeasureRepository

class MainActivityViewModel @ViewModelInject constructor(
  private val categoryRepository: CategoryRepository,
  private val measureRepository: MeasureRepository
) : ViewModel() {
  fun initData() {
    categoryRepository.init()
    measureRepository.init()
  }
}