package com.hotmail.leon.zimmermann.homeassistant.app.ui.management

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product.ProductRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.TemplateRepository

class ManagementViewModel : ViewModel() {
    val products: MutableLiveData<MutableList<Product>> = ProductRepository.products
    val templates: MutableLiveData<MutableList<Template>> = TemplateRepository.templates
    val meals: MutableLiveData<MutableList<Meal>> = MealRepository.meals

    var mode: MutableLiveData<ManagementFragment.Mode> = MutableLiveData(ManagementFragment.Mode.PRODUCT)
}