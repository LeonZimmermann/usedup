package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.creation

import android.app.Application
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.meal.MealIngredientEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.meal.Meal
import com.hotmail.leon.zimmermann.homeassistant.models.tables.meal.MealMetaDataEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.meal.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductRepository
import kotlinx.coroutines.launch

class MealCreationViewModel(application: Application) : AndroidViewModel(application) {
    var nameString = MutableLiveData<String>()
    var durationString = MutableLiveData<String>()
    var descriptionString = MutableLiveData<String>()
    var instructionsString = MutableLiveData<String>()
    val mealTemplateList: MutableLiveData<MutableList<MealTemplate>> by lazy {
        MutableLiveData<MutableList<MealTemplate>>().apply {
            value = mutableListOf()
        }
    }

    private val mealRepository: MealRepository
    val productList: LiveData<List<ProductEntity>>

    private val name: String?
        get() = nameString.value
    private val duration: Int?
        get() = durationString.value?.toInt()
    private val description: String?
        get() = descriptionString.value
    private val instructions: String?
        get() = instructionsString.value

    init {
        val database = HomeAssistantDatabase.getDatabase(application, viewModelScope)
        mealRepository = MealRepository(database.mealDao())
        val productRepository = ProductRepository(database.productDao())
        productList = productRepository.productEntityList
    }

    fun addConsumptionTemplate(mealTemplate: MealTemplate) {
        val consumptionTemplateList = mealTemplateList.value
        consumptionTemplateList?.add(mealTemplate)
        this.mealTemplateList.value = consumptionTemplateList
    }

    fun saveDinnerToDatabase() {
        // TODO Check if name is null and consumptionList empty
        viewModelScope.launch {
            mealRepository.insert(
                Meal(
                    MealMetaDataEntity(name!!, duration, description, instructions),
                    mealTemplateList.value!!.toList().map { MealIngredientEntity(it.product.id, it.measure.id, it.value) }
                )
            )
        }
    }
}
