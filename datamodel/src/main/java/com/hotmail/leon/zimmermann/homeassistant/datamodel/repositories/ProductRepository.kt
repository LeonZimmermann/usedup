package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseCategory
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseMeasure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product

object ProductRepository {
    private val database = Firebase.firestore

    val products: MutableList<Product> = mutableListOf()

    fun init() {
        Tasks.await(database.collection(FirebaseProduct.COLLECTION_NAME).get()).forEach { document ->
            products.add(Product.createInstance(document.id, document.toObject()))
        }
    }

    fun getProductForId(id: String): Product = products.first { it.id == id }
    fun getProductForName(name: String): Product = products.first { it.name == name }

    fun addProduct(
        name: String,
        categoryId: String,
        capacity: Double,
        measureId: String,
        quantity: Double,
        min: Int,
        max: Int
    ): Task<DocumentReference> {
        val measureReference = database.collection(FirebaseMeasure.COLLECTION_NAME).document(measureId)
        val categoryReference = database.collection(FirebaseCategory.COLLECTION_NAME).document(categoryId)
        val product = mapOf(
            "name" to name, "quantity" to quantity, "min" to min, "max" to max, "capacity" to capacity,
            "measureReference" to measureReference,
            "categoryReference" to categoryReference
        )
        return database.collection(FirebaseProduct.COLLECTION_NAME)
            .add(product)
            .addOnSuccessListener { reference ->
                products.add(
                    Product.createInstance(
                        reference.id,
                        FirebaseProduct(name, quantity, min, max, capacity, measureReference, categoryReference)
                    )
                )
            }
    }

    fun updateProduct(
        id: String,
        name: String,
        categoryId: String,
        capacity: Double,
        measureId: String,
        quantity: Double,
        min: Int,
        max: Int
    ): Task<Void> {
        val measureReference = database.collection(FirebaseMeasure.COLLECTION_NAME).document(measureId)
        val categoryReference = database.collection(FirebaseCategory.COLLECTION_NAME).document(categoryId)
        val data = mapOf(
            "name" to name, "quantity" to quantity, "min" to min, "max" to max, "capacity" to capacity,
            "measureReference" to measureReference,
            "categoryReference" to categoryReference
        )
        return database.collection(FirebaseProduct.COLLECTION_NAME)
            .document(id)
            .update(data)
            .addOnSuccessListener {
                getProductForId(id).apply {
                    this.name = name
                    this.categoryId = categoryId
                    this.capacity = capacity
                    this.measureId = measureId
                    this.quantity = quantity
                    this.min = min
                    this.max = max
                }
            }
    }

    fun deleteProduct(productId: String): Task<Void> {
        // TODO Account for changes in templates and meals
        products.remove(getProductForId(productId))
        return database.collection(FirebaseProduct.COLLECTION_NAME).document(productId).delete()
    }

}