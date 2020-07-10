package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.management

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.TemplateRepository

class ManagementViewModel : ViewModel() {
    val products: MutableList<Product> = ProductRepository.products
    val templates: MutableList<Template> = TemplateRepository.templates
    val meals: MutableList<Meal> = MealRepository.meals

    var mode: MutableLiveData<ManagementFragment.Mode> = MutableLiveData(ManagementFragment.Mode.PRODUCT)
}