package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import kotlinx.coroutines.*
import java.io.IOException

class QuantityDatabaseProcessor {

  @Throws(IOException::class)
  fun updateSingleProductQuantity(product: Product, updatedQuantity: Double) = runBlocking(Dispatchers.IO) {
    Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME)
      .document(product.id)
      .update(mapOf("quantity" to updatedQuantity))
      .addOnFailureListener { throw IOException() }
  }

  @Throws(IOException::class)
  fun updateMultipleProductQuantities(data: List<Pair<Product, Double>>) = runBlocking(Dispatchers.IO) {
    val productCollection = Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME)
    Firebase.firestore.batch().apply {
      data.forEach { (product, updatedQuantity) ->
        update(productCollection.document(product.id), mapOf("quantity" to updatedQuantity))
      }
    }.commit().addOnFailureListener { throw IOException() }
  }
}