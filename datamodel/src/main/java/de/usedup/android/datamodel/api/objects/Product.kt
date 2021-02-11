package de.usedup.android.datamodel.api.objects

import de.usedup.android.datamodel.api.calculateDiscrepancy
import de.usedup.android.datamodel.api.exceptions.DataIntegrityException
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import de.usedup.android.datamodel.firebase.objects.FirebaseProduct

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