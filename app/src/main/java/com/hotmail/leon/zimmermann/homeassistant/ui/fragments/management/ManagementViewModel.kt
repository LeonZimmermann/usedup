package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.management

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.TemplateRepository

class ManagementViewModel : ViewModel() {
    val products: List<Product> = ProductRepository.products
    val templates: List<Template> = TemplateRepository.templates
    val meals: List<Meal> = MealRepository.meals

    var mode: MutableLiveData<ManagementFragment.Mode> = MutableLiveData(ManagementFragment.Mode.PRODUCT)
}