package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Meal

object MealRepository {
    val meals: MutableList<Meal> by lazy {
        val list: MutableList<Meal> = mutableListOf()
        Firebase.firestore.collection(Meal.COLLECTION_NAME).get().addOnSuccessListener { documents ->
            for (document in documents)
                list.add(document.toObject())
        }
        list
    }

    fun getMealForId(id: String) = meals.first { it.id == id }
    fun getMealForName(name: String) = meals.first { it.name == name }

    fun deleteMeal(id: String) {
        TODO("Implement")
    }
}