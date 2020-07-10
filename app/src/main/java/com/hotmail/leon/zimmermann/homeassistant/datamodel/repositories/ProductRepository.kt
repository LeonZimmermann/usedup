package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*

object ProductRepository {
    private val database = Firebase.firestore
    val products: MutableList<Product> by lazy {
        val list: MutableList<Product> = mutableListOf()
        database.collection(Product.COLLECTION_NAME).get().addOnSuccessListener { documents ->
            for (document in documents) list.add(document.toObject())
        }
        list
    }

    fun getProductForId(id: String): Product = products.first { it.id == id }
    fun getProductForName(name: String): Product = products.first { it.name == name }

    fun addProduct(
        name: String,
        category: Category,
        capacity: Double,
        measure: Measure,
        quantity: Double,
        min: Int,
        max: Int
    ): Task<DocumentReference> {
        val product = Product(
            name, quantity, min, max, capacity,
            database.collection(Measure.COLLECTION_NAME).document(measure.id),
            database.collection(Category.COLLECTION_NAME).document(category.id)
        )
        return database.collection(Product.COLLECTION_NAME)
            .add(product)
            .addOnSuccessListener {
                product.id = it.id
                products.add(product)
            }
    }

    // TODO Account for changes in templates and meals
    fun updateProduct(
        productId: String,
        name: String,
        category: Category,
        capacity: Double,
        measure: Measure,
        quantity: Double,
        min: Int,
        max: Int
    ): Task<Void> {
        val data = mapOf(
            "name" to name,
            "category" to database.collection(Category.COLLECTION_NAME).document(category.id!!),
            "capacity" to capacity,
            "measure" to database.collection(Measure.COLLECTION_NAME).document(measure.id),
            "quantity" to quantity,
            "min" to min,
            "max" to max
        )
        getProductForId(productId).apply {
            this.name = name
            this.category = database.collection(Category.COLLECTION_NAME).document(category.id!!)
            this.capacity = capacity
            this.measure = database.collection(Measure.COLLECTION_NAME).document(measure.id)
            this.quantity = quantity
            this.min = min
            this.max = max
        }
        return database.collection(Product.COLLECTION_NAME)
            .document(productId)
            .update(data)
    }

    // TODO Account for changes in templates and meals
    fun deleteProduct(productId: String): Task<Void> {
        products.remove(getProductForId(productId))
        return database.collection(Product.COLLECTION_NAME).document(productId).delete()
    }

}