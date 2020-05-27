package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview.consumption

import android.app.Application
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.*

class ConsumptionViewModel(application: Application) : AndroidViewModel(application) {

    val productList: List<Product> = emptyList()
    val mealWithIngredientsList: List<Meal> = emptyList()
    val templateList: List<Template> = emptyList()

    val measures: List<Measure> = emptyList()
    val categories: List<Category> = emptyList()

    var productMode: ProductMode
    var templateMode: TemplateMode
    var mealMode: MealMode
    var consumptionMode: MutableLiveData<ConsumptionMode>

    init {
        // TODO Init lists
        productMode = ProductMode(this)
        templateMode = TemplateMode(this)
        mealMode = MealMode(this)
        this.consumptionMode = MutableLiveData(productMode)
    }
}