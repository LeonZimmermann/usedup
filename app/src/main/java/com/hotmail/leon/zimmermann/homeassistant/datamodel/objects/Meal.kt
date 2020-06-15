package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

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

object MealRepository {
    val meals: MutableList<Pair<String, Meal>> by lazy {
        val list: MutableList<Pair<String, Meal>> = mutableListOf()
        Firebase.firestore.collection(Meal.COLLECTION_NAME).get().addOnSuccessListener { documents ->
            for (document in documents)
                list.add(Pair(document.id, document.toObject()))
        }
        list
    }

    fun getId(mealName: String) = meals.first { it.second.name == mealName }.first
    fun getMealForId(id: String) = meals.first { it.first == id }.second
    fun getMealForName(name: String) = meals.first { it.second.name == name }.second
}