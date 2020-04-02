package com.hotmail.leon.zimmermann.homeassistant.models.tables.meal

class MealRepository(private val mealDao: MealDao) {

    val mealList = mealDao.getAll()

    suspend fun insert(meal: Meal) {
        mealDao.insert(meal)
    }
}