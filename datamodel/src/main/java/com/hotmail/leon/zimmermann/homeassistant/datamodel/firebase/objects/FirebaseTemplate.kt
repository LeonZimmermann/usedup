package com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories.FirebaseUserRepository

internal class FirebaseTemplate(
  @PropertyName("name") var name: String? = null,
  @PropertyName("components") var components: List<FirebaseTemplateComponent>? = null,
  @PropertyName("userId") var userId: String? = null
) {
  companion object {
    const val COLLECTION_NAME = "templates"
  }
}

internal class FirebaseTemplateComponent(
  @PropertyName("productReference") var product: DocumentReference? = null,
  @PropertyName("measureReference") var measure: DocumentReference? = null,
  @PropertyName("value") var value: Double? = null
)