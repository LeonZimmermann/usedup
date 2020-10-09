package com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories.FirebaseUserRepository

internal class FirebaseMeal(
  @PropertyName("name") var name: String? = null,
  @PropertyName("duration") var duration: Int? = null,
  @PropertyName("description") var description: String? = null,
  @PropertyName("instructions") var instructions: String? = null,
  @PropertyName("backgroundUrl") var backgroundUrl: String? = null,
  @PropertyName("ingredients") var ingredients: List<FirebaseMealIngredient>? = null,
  @PropertyName("userId") var userId: String? = null
) {
  companion object {
    const val COLLECTION_NAME = "meals"
  }
}

internal class FirebaseMealIngredient(
  @PropertyName("productReference") var product: DocumentReference? = null,
  @PropertyName("measureReference") var measure: DocumentReference? = null,
  @PropertyName("value") var value: Double? = null
)