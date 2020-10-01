package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import kotlinx.coroutines.*

class ConsumptionDatabaseProcessor {

  fun updateSingleProductQuantity(product: Product, updatedQuantity: Double) = runBlocking(Dispatchers.IO) {
    Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME)
      .document(product.id)
      .update(mapOf("quantity" to updatedQuantity))
  }

  fun updateMultipleProductQuantities(data: List<Pair<Product, Double>>) = runBlocking(Dispatchers.IO) {
    val productCollection = Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME)
    Firebase.firestore.batch().apply {
      data.forEach { (product, updatedQuantity) ->
        update(productCollection.document(product.id), mapOf("quantity" to updatedQuantity))
      }
    }.commit()
  }
}