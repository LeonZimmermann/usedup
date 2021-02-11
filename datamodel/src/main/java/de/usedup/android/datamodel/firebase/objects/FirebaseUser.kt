package de.usedup.android.datamodel.firebase.objects

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

internal data class FirebaseUser(
  @DocumentId @PropertyName("id") val id: String? = null,
  @PropertyName("name") val name: String? = null,
  @PropertyName("email") val email: String? = null
) {
  companion object {
    const val COLLECTION_NAME = "users"
  }
}