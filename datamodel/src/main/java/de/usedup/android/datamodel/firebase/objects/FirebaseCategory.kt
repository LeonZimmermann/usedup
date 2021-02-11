package de.usedup.android.datamodel.firebase.objects

import com.google.firebase.firestore.PropertyName

internal data class FirebaseCategory(
    @PropertyName("name") var name: String? = null,
    @PropertyName("position") var position: Int? = null
) {
  companion object {
    const val COLLECTION_NAME = "categories"
  }
}