package com.hotmail.leon.zimmermann.homeassistant.models.tables.meal

import androidx.room.*
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import java.io.Serializable

data class MealWithIngredients(
    @Embedded
    val meal: MealEntity,
    @Relation(parentColumn = "meal_id", entityColumn = "meal_id", entity = MealIngredientEntity::class)
    val ingredients: List<MealIngredientEntity>
) : Serializable