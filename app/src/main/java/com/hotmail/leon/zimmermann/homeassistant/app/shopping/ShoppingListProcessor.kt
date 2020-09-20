package com.hotmail.leon.zimmermann.homeassistant.app.shopping

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseProduct

class ShoppingListProcessor(private val shoppingViewModel: ShoppingViewModel) {
    private val database: FirebaseFirestore = shoppingViewModel.database

    fun process(list: List<ShoppingProduct>) {
        val listToProcess = list.filter { it.checked }
        updateDatabase(listToProcess).addOnSuccessListener {
            updateReferences(listToProcess)
        }.addOnFailureListener {
            shoppingViewModel.systemMessage.value = it.message
        }
    }

    private fun updateDatabase(list: List<ShoppingProduct>): Task<Void> {
        val collectionReference = database.collection(FirebaseProduct.COLLECTION_NAME)
        return database.batch().apply {
            list.forEach {
                update(collectionReference.document(it.product.id), mapOf("quantity" to calculateNewQuantity(it)))
            }
        }.commit()
    }

    private fun updateReferences(list: List<ShoppingProduct>) {
        list.forEach {
            it.product.quantity = calculateNewQuantity(it)
        }
    }

    private fun calculateNewQuantity(shoppingProduct: ShoppingProduct) =
        shoppingProduct.product.quantity + shoppingProduct.cartAmount
}