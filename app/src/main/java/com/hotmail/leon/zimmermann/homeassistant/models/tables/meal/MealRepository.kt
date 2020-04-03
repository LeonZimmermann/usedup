package com.hotmail.leon.zimmermann.homeassistant.models.tables.meal

import androidx.lifecycle.Transformations

class MealRepository(private val mealDao: MealDao) {

    val mealList = Transformations.map(mealDao.getAll()) { mealList -> mealList.map { Meal(it) } }

    suspend fun insert(meal: Meal) {
        mealDao.insert(meal)
    }
}