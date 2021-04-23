package de.usedup.android.datamodel.firebase.objects

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class FirebasePlannerItem(
  @PropertyName("mealReference") val mealReference: DocumentReference? = null,
  @PropertyName("date") val date: Long? = null,
  @PropertyName("householdReference") var householdReference: DocumentReference? = null
) {
  companion object {
    const val COLLECTION_NAME = "planners"
  }
}