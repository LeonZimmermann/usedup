package com.hotmail.leon.zimmermann.homeassistant.models.tables.meal

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class MealDao {

    @Query("SELECT * FROM meals")
    abstract fun getAll(): LiveData<List<Meal>>

    suspend fun insert(meal: Meal) {
        insert(meal.metaData)
        insert(meal.mealIngredients)
    }

    @Insert
    protected abstract suspend fun insert(mealIngredientEntityList: List<MealIngredientEntity>)

    @Insert
    protected abstract suspend fun insert(mealMetaDataEntity: MealMetaDataEntity)

}