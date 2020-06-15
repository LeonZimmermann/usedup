package com.hotmail.leon.zimmermann.homeassistant.datamodel

import com.google.firebase.firestore.DocumentReference

data class Meal(val name: String? = null,
                val duration: Int? = null,
                val description: String? = null,
                val instructions: String? = null,
                val backgroundUrl: String? = null,
                val ingredients: List<MealIngredient>? = null) {
    companion object {
        const val COLLECTION_NAME = "meals"
    }
}

data class MealIngredient(
    val product: DocumentReference? = null,
    val measure: DocumentReference? = null,
    var value: Double? = null
)