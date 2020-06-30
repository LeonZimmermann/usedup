package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.management

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*

class ManagementViewModel : ViewModel() {
    val products: List<Pair<String, Product>> = ProductRepository.products
    val templates: List<Pair<String, Template>> = TemplateRepository.templates
    val meals: List<Pair<String, Meal>> = MealRepository.meals

    var mode: MutableLiveData<ManagementFragment.Mode> = MutableLiveData(ManagementFragment.Mode.PRODUCT)
}