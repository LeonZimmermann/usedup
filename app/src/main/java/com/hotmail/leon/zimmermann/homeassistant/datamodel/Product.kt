package com.hotmail.leon.zimmermann.homeassistant.datamodel

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.math.floor
import kotlin.math.max

data class Product(
    var name: String = "",
    var quantity: Double = 0.0,
    var min: Int = 0,
    var max: Int = 0,
    var capacity: Double = 0.0,
    var measure: DocumentReference? = null,
    var category: DocumentReference? = null
) {
    val discrepancy: Int
        get() = max(max - floor(quantity).toInt(), 0)

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
}