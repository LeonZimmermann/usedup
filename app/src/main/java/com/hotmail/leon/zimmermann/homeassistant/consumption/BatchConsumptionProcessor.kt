package com.hotmail.leon.zimmermann.homeassistant.consumption

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.NotEnoughException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.ValueWithMeasure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class BatchConsumptionProcessor(
    private val database: FirebaseFirestore,
    private val scope: CoroutineScope
) : BatchConsumptionBuilder.ResultHandler {
    override fun onSuccess(batch: List<Pair<Product, Double>>) {
        scope.launch {
            updateDatabase(batch).addOnSuccessListener {
                updateReferences(batch)
            }
        }
    }

    private fun updateDatabase(batch: List<Pair<Product, Double>>): Task<Void> {
        return database.batch().apply {
            batch.forEach {
                val (product, newQuantity) = it
                update(
                    database.collection(FirebaseProduct.COLLECTION_NAME).document(product.id),
                    mapOf("quantity" to newQuantity)
                )
            }
        }.commit()
    }

    private fun updateReferences(batch: List<Pair<Product, Double>>) {
        batch.forEach {
            val (product, newQuantity) = it
            product.quantity = newQuantity
        }
    }

    override fun onFailure(missingQuantities: List<Pair<Product, ValueWithMeasure>>) {
        throw NotEnoughException(missingQuantities)
    }
}