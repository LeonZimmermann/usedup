package com.hotmail.leon.zimmermann.homeassistant.app.management

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Meal
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Template
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.TemplateRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository

class ManagementViewModel @ViewModelInject constructor(
    productRepository: ProductRepository,
    templateRepository: TemplateRepository,
    mealRepository: MealRepository
) : ViewModel() {

  val products: MutableLiveData<MutableList<Product>> = productRepository.products
  val templates: MutableLiveData<MutableList<Template>> = templateRepository.templates
  val meals: MutableLiveData<MutableList<Meal>> = mealRepository.meals

  var mode: MutableLiveData<ManagementFragment.Mode> = MutableLiveData(ManagementFragment.Mode.PRODUCT)
}