package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview.consumption

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.TemplateRepository

class ConsumptionViewModel(application: Application) : AndroidViewModel(application) {
    val database = Firebase.firestore
    val productList: MutableList<Product> = ProductRepository.products
    val mealList: MutableList<Meal> = MealRepository.meals
    val templateList: MutableList<Template> = TemplateRepository.templates
    val measures: MutableList<Measure> = MeasureRepository.measures

    var productMode: ProductMode = ProductMode(this)
    var templateMode: TemplateMode = TemplateMode(this)
    var mealMode: MealMode = MealMode(this)
    var consumptionMode: MutableLiveData<ConsumptionMode> = MutableLiveData()

}