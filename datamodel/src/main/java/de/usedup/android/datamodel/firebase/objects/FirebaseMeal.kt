package de.usedup.android.datamodel.firebase.objects

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

internal class FirebaseMeal(
  @PropertyName("name") var name: String? = null,
  @PropertyName("duration") var duration: Int? = null,
  @PropertyName("description") var description: String? = null,
  @PropertyName("instructions") var instructions: String? = null,
  @PropertyName("backgroundUrl") var backgroundUrl: String? = null,
  @PropertyName("ingredients") var ingredients: List<FirebaseMealIngredient>? = null,
  @PropertyName("householdReference") var householdReference: DocumentReference? = null,
) {
  companion object {
    const val COLLECTION_NAME = "meals"
  }
}

internal class FirebaseMealIngredient(
  @PropertyName("productReference") var productReference: DocumentReference? = null,
  @PropertyName("measureReference") var measureReference: DocumentReference? = null,
  @PropertyName("value") var value: Double? = null,
)