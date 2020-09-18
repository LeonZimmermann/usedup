package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.DataIntegrityException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseMeal
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseMealIngredient

class Meal(
    var id: String,
    var name: String,
    var duration: Int,
    var description: String?,
    var instructions: String?,
    var backgroundUrl: String?,
    var ingredients: List<MealIngredient>
) {
    companion object {
        fun createInstance(id: String, firebaseObject: FirebaseMeal): Meal {
            val name = firebaseObject.name ?: throw DataIntegrityException()
            val duration = firebaseObject.duration ?: throw DataIntegrityException()
            val description = firebaseObject.description
            val instructions = firebaseObject.instructions
            val backgroundUrl = firebaseObject.backgroundUrl
            val ingredients = firebaseObject.ingredients
                ?.map { MealIngredient.createInstance(it) }
                ?: throw DataIntegrityException()
            return Meal(id, name, duration, description, instructions, backgroundUrl, ingredients)
        }
    }
}

class MealIngredient(
    var productId: String,
    var measureId: String,
    var value: Double
) {
    companion object {
        fun createInstance(firebaseObject: FirebaseMealIngredient): MealIngredient {
            val productId = firebaseObject.product?.id ?: throw DataIntegrityException()
            val measureId = firebaseObject.measure?.id ?: throw DataIntegrityException()
            val value = firebaseObject.value ?: throw DataIntegrityException()
            return MealIngredient(productId, measureId, value)
        }
    }
}