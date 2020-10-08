package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.exceptions.DataIntegrityException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseId
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseMeal
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseMealIngredient

class Meal(
    var id: Id,
    var name: String,
    var duration: Int,
    var description: String?,
    var instructions: String?,
    var backgroundUrl: String?,
    var ingredients: List<MealIngredient>
) {
    companion object {
        internal fun createInstance(id: String, firebaseObject: FirebaseMeal): Meal {
            val name = firebaseObject.name ?: throw DataIntegrityException()
            val duration = firebaseObject.duration ?: throw DataIntegrityException()
            val description = firebaseObject.description
            val instructions = firebaseObject.instructions
            val backgroundUrl = firebaseObject.backgroundUrl
            val ingredients = firebaseObject.ingredients
                ?.map { MealIngredient.createInstance(it) }
                ?: throw DataIntegrityException()
            return Meal(FirebaseId(id), name, duration, description, instructions, backgroundUrl, ingredients)
        }
    }
}

class MealIngredient(
    var productId: Id,
    var measureId: Id,
    var value: Double
) {
    companion object {
        internal fun createInstance(firebaseObject: FirebaseMealIngredient): MealIngredient {
            val productId = FirebaseId(firebaseObject.product?.id ?: throw DataIntegrityException())
            val measureId = FirebaseId(firebaseObject.measure?.id ?: throw DataIntegrityException())
            val value = firebaseObject.value ?: throw DataIntegrityException()
            return MealIngredient(productId, measureId, value)
        }
    }
}