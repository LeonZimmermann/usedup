package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.DataIntegrityException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseProduct
import kotlin.math.floor
import kotlin.math.max

data class Product(
    var id: String,
    var name: String,
    var quantity: Double,
    var min: Int,
    var max: Int,
    var capacity: Double,
    var measureId: String,
    var categoryId: String
) {
  val discrepancy: Int
    get() = max(min - floor(quantity).toInt(), 0)

  companion object {
    fun createInstance(id: String, firebaseObject: FirebaseProduct): Product {
      val name = firebaseObject.name ?: throw DataIntegrityException()
      val quantity = firebaseObject.quantity ?: throw DataIntegrityException()
      val min = firebaseObject.min ?: throw DataIntegrityException()
      val max = firebaseObject.max ?: throw DataIntegrityException()
      val capacity = firebaseObject.capacity ?: throw DataIntegrityException()
      val measureId = firebaseObject.measureReference?.id ?: throw DataIntegrityException()
      val categoryId = firebaseObject.categoryReference?.id ?: throw DataIntegrityException()
      return Product(id, name, quantity, min, max, capacity, measureId, categoryId)
    }


  }
}