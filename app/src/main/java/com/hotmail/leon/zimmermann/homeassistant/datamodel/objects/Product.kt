package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.math.floor
import kotlin.math.max

data class Product(
    var name: String? = null,
    var quantity: Double? = null,
    var min: Int? = null,
    var max: Int? = null,
    var capacity: Double? = null,
    var measure: DocumentReference? = null,
    var category: DocumentReference? = null
) {
    val discrepancy: Int
        get() = max(max!! - floor(quantity!!).toInt(), 0)

    companion object {
        const val COLLECTION_NAME = "products"
    }
}

object ProductRepository {
    val products: MutableList<Pair<String, Product>> by lazy {
        val list: MutableList<Pair<String, Product>> = mutableListOf()
        Firebase.firestore.collection(Product.COLLECTION_NAME).get().addOnSuccessListener { documents ->
            for (document in documents)
                list.add(Pair(document.id, document.toObject()))
        }
        list
    }

    fun getId(productName: String) = products.first { it.second.name == productName }.first
    fun getProductForId(id: String) = products.first { it.first == id }.second
    fun getProductForName(name: String) = products.first { it.second.name == name }.second
}