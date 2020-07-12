package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*

object MealRepository {
    private val database = Firebase.firestore

    val meals: MutableList<Meal> = mutableListOf()

    fun init() {
        Tasks.await(Firebase.firestore.collection(Meal.COLLECTION_NAME).get()).forEach { document ->
            meals.add(document.toObject())
        }
    }

    fun getMealForId(id: String) = meals.first { it.id == id }
    fun getMealForName(name: String) = meals.first { it.name == name }

    fun addMeal(
        name: String,
        duration: Int,
        description: String,
        instructions: String,
        backgroundUrl: String?,
        ingredients: List<MealIngredient>
    ): Task<DocumentReference> {
        val meal = Meal(name, duration, description, instructions, backgroundUrl, ingredients)
        return database.collection(Meal.COLLECTION_NAME)
            .add(meal)
            .addOnSuccessListener {
                meal.id = it.id
                meals.add(meal)
            }
    }

    fun updateMeal(
        mealId: String,
        name: String,
        duration: Int,
        description: String,
        instructions: String,
        backgroundUrl: String?,
        ingredients: List<MealIngredient>
    ): Task<Void> {
        val data = mapOf(
            "name" to name,
            "duration" to duration,
            "description" to description,
            "name" to instructions,
            "backgroundUrl" to backgroundUrl,
            "ingredients" to ingredients
        )
        return database.collection(Meal.COLLECTION_NAME)
            .document(mealId)
            .update(data)
            .addOnSuccessListener {
                getMealForId(mealId).apply {
                    this.name = name
                    this.duration = duration
                    this.description = description
                    this.instructions = instructions
                    this.backgroundUrl = backgroundUrl
                    this.ingredients = ingredients
                }
            }
    }

    fun deleteMeal(mealId: String): Task<Void> {
        meals.remove(getMealForId(mealId))
        return database.collection(Meal.COLLECTION_NAME).document(mealId).delete()
    }
}