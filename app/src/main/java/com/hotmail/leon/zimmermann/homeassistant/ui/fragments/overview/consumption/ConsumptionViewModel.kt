package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview.consumption

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*

class ConsumptionViewModel(application: Application) : AndroidViewModel(application) {
    val database = Firebase.firestore
    val productList: MutableList<Pair<String, Product>> = ProductRepository.products
    val mealList: MutableList<Pair<String, Meal>> = MealRepository.meals
    val templateList: MutableList<Pair<String, Template>> = TemplateRepository.templates
    val measures: MutableList<Pair<String, Measure>> = MeasureRepository.measures

    var productMode: ProductMode = ProductMode(this)
    var templateMode: TemplateMode = TemplateMode(this)
    var mealMode: MealMode = MealMode(this)
    var consumptionMode: MutableLiveData<ConsumptionMode> = MutableLiveData()

}