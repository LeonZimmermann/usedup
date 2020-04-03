package com.hotmail.leon.zimmermann.homeassistant.models.tables.meal

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class MealDao {

    @Query("SELECT * FROM meals")
    abstract fun getAll(): LiveData<List<MealWithIngredients>>

    suspend fun insert(meal: Meal) {
        val mealId = insert(MealEntity(meal.name, meal.duration, meal.description, meal.instructions, meal.backgroundUrl))
        val ingredients = meal.ingredients.map { MealIngredientEntity(mealId, it.productId, it.value, it.measureId) }
        insert(ingredients)
    }

    @Insert
    protected abstract suspend fun insert(mealIngredientEntityList: List<MealIngredientEntity>)

    @Insert
    protected abstract suspend fun insert(mealEntity: MealEntity): Long

}