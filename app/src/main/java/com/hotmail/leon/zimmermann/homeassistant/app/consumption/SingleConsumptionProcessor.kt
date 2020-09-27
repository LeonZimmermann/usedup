package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import com.google.firebase.firestore.FirebaseFirestore
import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.NotEnoughException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.MeasureValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SingleConsumptionProcessor(
    private val database: FirebaseFirestore,
    private val scope: CoroutineScope
): SingleConsumptionBuilder.ResultHandler {
    override fun onSuccess(newQuantity: Pair<Product, Double>) {
        scope.launch {
            val (product, quantityValue) = newQuantity
            database.collection(FirebaseProduct.COLLECTION_NAME)
                .document(product.id)
                .update(mapOf("quantity" to quantityValue))
                .addOnSuccessListener { product.quantity = quantityValue }
        }
    }

    override fun onFailure(missingQuantity: Pair<Product, MeasureValue>) {
        throw NotEnoughException(listOf(missingQuantity))
    }
}