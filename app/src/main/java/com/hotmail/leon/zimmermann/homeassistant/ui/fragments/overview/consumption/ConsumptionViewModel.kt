package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview.consumption

import android.app.Application
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.meal.Meal
import com.hotmail.leon.zimmermann.homeassistant.models.tables.meal.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductRepository
import com.hotmail.leon.zimmermann.homeassistant.models.tables.template.Template
import com.hotmail.leon.zimmermann.homeassistant.models.tables.template.TemplateRepository
import kotlin.contracts.contract

class ConsumptionViewModel(application: Application) : AndroidViewModel(application) {

    val productList: LiveData<List<ProductEntity>>
    val mealList: LiveData<List<Meal>>
    val templateList: LiveData<List<Template>>

    var productMode: ProductMode
    var templateMode: TemplateMode
    var mealMode: MealMode
    var consumptionMode: MutableLiveData<ConsumptionMode>

    init {
        val database = HomeAssistantDatabase.getDatabase(application, viewModelScope)
        val productRepository = ProductRepository(database.productDao())
        productList = productRepository.productEntityList
        val mealRepository = MealRepository(database.mealDao())
        mealList = mealRepository.mealList
        val templateRepository = TemplateRepository(database.templateDao())
        templateList = templateRepository.templateList
        productMode = ProductMode(this)
        templateMode = TemplateMode(this)
        mealMode = MealMode(this)
        this.consumptionMode = MutableLiveData(productMode)
    }
}