package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.calculateDiscrepancy
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.exceptions.DataIntegrityException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseId
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseProduct
import kotlin.math.floor
import kotlin.math.max

data class Product(
    val id: Id,
    var name: String,
    var quantity: Double,
    var min: Int,
    var max: Int,
    var capacity: Double,
    var measureId: Id,
    var categoryId: Id
) {
  val discrepancy: Int
    get() = calculateDiscrepancy(min, quantity)

  companion object {
    internal fun createInstance(id: String, firebaseObject: FirebaseProduct): Product {
      val name = firebaseObject.name ?: throw DataIntegrityException()
      val quantity = firebaseObject.quantity ?: throw DataIntegrityException()
      val min = firebaseObject.min ?: throw DataIntegrityException()
      val max = firebaseObject.max ?: throw DataIntegrityException()
      val capacity = firebaseObject.capacity ?: throw DataIntegrityException()
      val measureId = FirebaseId(firebaseObject.measureReference?.id ?: throw DataIntegrityException())
      val categoryId = FirebaseId(firebaseObject.categoryReference?.id ?: throw DataIntegrityException())
      return Product(FirebaseId(id), name, quantity, min, max, capacity, measureId, categoryId)
    }


  }
}