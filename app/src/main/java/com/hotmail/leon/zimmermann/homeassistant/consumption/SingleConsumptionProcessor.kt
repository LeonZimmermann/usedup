package com.hotmail.leon.zimmermann.homeassistant.consumption

import com.google.firebase.firestore.FirebaseFirestore
import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.NotEnoughException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SingleConsumptionProcessor(
    private val database: FirebaseFirestore,
    private val scope: CoroutineScope
): SingleConsumptionBuilder.ResultHandler {
    override fun onSuccess(newQuantity: Pair<Product, Double>) {
        scope.launch {
            val (product, quantityValue) = newQuantity
            database.collection(Product.COLLECTION_NAME)
                .document(product.id)
                .update(mapOf("quantity" to quantityValue))
                .addOnSuccessListener { product.quantity = quantityValue }
        }
    }

    override fun onFailure(missingQuantity: Pair<Product, Value>) {
        throw NotEnoughException(listOf(missingQuantity))
    }
}