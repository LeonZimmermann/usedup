package com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects

import com.google.firebase.firestore.PropertyName

internal class FirebaseMeasure(
  @PropertyName("name") var name: String? = null,
  @PropertyName("abbreviation") var abbreviation: String? = null,
  @PropertyName("baseFactor") var baseFactor: Float? = null,
  @PropertyName("type") var type: String? = null,
  @PropertyName("complex") var complex: Boolean? = null
) {
  companion object {
    const val COLLECTION_NAME = "measures"
  }
}