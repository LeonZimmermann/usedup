package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

class Meal(
    @PropertyName("name") private var _name: String? = null,
    @PropertyName("duration") private var _duration: Int? = null,
    @PropertyName("description") private var _description: String? = null,
    @PropertyName("instructions") private var _instructions: String? = null,
    @PropertyName("backgroundUrl") var backgroundUrl: String? = null,
    @PropertyName("ingredients") private var _ingredients: List<MealIngredient>? = null
) {
    @DocumentId lateinit var id: String
    var name: String
        set(value) {
            _name = value
        }
        get() = _name!!
    var duration: Int
        set(value) {
            _duration = value
        }
        get() = _duration!!
    var description: String
        set(value) {
            _description = value
        }
        get() = _description!!
    var instructions: String
        set(value) {
            _instructions = value
        }
        get() = _instructions!!
    var ingredients: List<MealIngredient>
        set(value) {
            _ingredients = value
        }
        get() = _ingredients!!

    companion object {
        const val COLLECTION_NAME = "meals"
    }
}

data class MealIngredient(
    val product: DocumentReference? = null,
    val measure: DocumentReference? = null,
    var value: Double? = null
)