package de.usedup.android.datamodel.firebase.objects

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

internal data class FirebaseHousehold(
  @PropertyName("adminReference") var adminReference: DocumentReference? = null,
  @PropertyName("memberReferences") var memberReferences: List<DocumentReference>? = null,
) {
  companion object {
    const val COLLECTION_NAME = "households"
  }
}