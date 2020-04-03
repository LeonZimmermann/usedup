package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.browser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.models.database.HomeAssistantDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.meal.Meal
import com.hotmail.leon.zimmermann.homeassistant.models.tables.meal.MealWithIngredients
import com.hotmail.leon.zimmermann.homeassistant.models.tables.meal.MealRepository

class MealBrowserViewModel(application: Application) : AndroidViewModel(application) {

    private val mealRepository: MealRepository
    val mealList: LiveData<List<Meal>>

    init {
        val database = HomeAssistantDatabase.getDatabase(application, viewModelScope)
        mealRepository = MealRepository(database.mealDao())
        mealList = mealRepository.mealList
    }
}
