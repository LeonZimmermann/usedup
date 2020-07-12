package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Category
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Measure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product

object ProductRepository {
    private val database = Firebase.firestore

    val products: MutableList<Product> = mutableListOf()

    fun init() {
        Tasks.await(database.collection(Product.COLLECTION_NAME).get()).forEach { document ->
            products.add(document.toObject())
        }
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
            "category" to database.collection(Category.COLLECTION_NAME).document(category.id),
            "capacity" to capacity,
            "measure" to database.collection(Measure.COLLECTION_NAME).document(measure.id),
            "quantity" to quantity,
            "min" to min,
            "max" to max
        )
        return database.collection(Product.COLLECTION_NAME)
            .document(productId)
            .update(data)
            .addOnSuccessListener {
                getProductForId(productId).apply {
                    this.name = name
                    this.category = database.collection(Category.COLLECTION_NAME).document(category.id)
                    this.capacity = capacity
                    this.measure = database.collection(Measure.COLLECTION_NAME).document(measure.id)
                    this.quantity = quantity
                    this.min = min
                    this.max = max
                }
            }
    }

    fun deleteProduct(productId: String): Task<Void> {
        // TODO Account for changes in templates and meals
        products.remove(getProductForId(productId))
        return database.collection(Product.COLLECTION_NAME).document(productId).delete()
    }

}