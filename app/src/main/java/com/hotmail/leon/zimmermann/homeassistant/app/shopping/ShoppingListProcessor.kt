package com.hotmail.leon.zimmermann.homeassistant.app.shopping

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseProduct
import com.hotmail.leon.zimmermann.homeassistant.app.ui.shopping.ShoppingProduct

class ShoppingListProcessor(private val database: FirebaseFirestore) {
    fun process(list: List<ShoppingProduct>) {
        updateDatabase(list).addOnSuccessListener {
            updateReferences(list)
        }
    }

    private fun updateDatabase(list: List<ShoppingProduct>): Task<Void> {
        return database.batch().apply {
            list.filter { it.checked }
                .forEach {
                    val newQuantity = it.product.quantity + it.cartAmount
                    update(
                        database.collection(FirebaseProduct.COLLECTION_NAME).document(it.product.id),
                        mapOf("quantity" to newQuantity)
                    )
                }
        }.commit()
    }

    private fun updateReferences(list: List<ShoppingProduct>) {
        list.filter { it.checked }
            .forEach {
                val newQuantity = it.product.quantity + it.cartAmount
                it.product.quantity = newQuantity
            }
    }
}