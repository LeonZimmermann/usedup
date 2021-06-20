package de.usedup.android.datamodel.firebase.objects

import com.google.firebase.firestore.PropertyName
import de.usedup.android.datamodel.api.objects.Id

data class FirebaseInvitation(
  @PropertyName("senderReference") val senderReference: Id? = null,
  @PropertyName("receiverReference") val receiverReference: Id? = null,
  @PropertyName("message") val message: String? = null,
) {
  companion object {
    const val COLLECTION_NAME = "invitation"
  }
}
