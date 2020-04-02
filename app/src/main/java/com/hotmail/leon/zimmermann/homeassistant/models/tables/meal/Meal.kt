package com.hotmail.leon.zimmermann.homeassistant.models.tables.meal

import androidx.room.*
import java.io.Serializable

data class Meal(
    @Embedded
    val metaData: MealMetaDataEntity,
    @Relation(parentColumn = "id", entityColumn = "list_id", entity = MealIngredientEntity::class)
    val mealIngredients: List<MealIngredientEntity>
) : Serializable