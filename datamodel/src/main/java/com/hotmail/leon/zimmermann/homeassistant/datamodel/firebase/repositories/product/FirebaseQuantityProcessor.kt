package com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories.product

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseId
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.IOException

internal class FirebaseQuantityProcessor {

  @Throws(IOException::class)
  fun updateSingleProductQuantity(product: Product, updatedQuantity: Double) = runBlocking(Dispatchers.IO) {
    Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME)
      .document((product.id as FirebaseId).value)
      .update(mapOf("quantity" to updatedQuantity))
      .addOnFailureListener { throw IOException() }
  }

  @Throws(IOException::class)
  fun updateMultipleProductQuantities(data: List<Pair<Product, Double>>) = runBlocking(Dispatchers.IO) {
    val productCollection = Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME)
    Firebase.firestore.batch().apply {
      data.forEach { (product, updatedQuantity) ->
        update(productCollection.document((product.id as FirebaseId).value), mapOf("quantity" to updatedQuantity))
      }
    }.commit().addOnFailureListener { throw IOException() }
  }
}