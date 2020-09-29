package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.NotEnoughException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.MeasureValue
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.toBase

class ConsumptionProcessor {

  var quantityDiff: Double = 0.0

  fun consume(product: Product, measureValue: MeasureValue): Double {
    val existingQuantity = product.quantity * product.capacity
    val conversionQuantity = measureValue.double.toBase(measureValue.measure)
    quantityDiff = existingQuantity - conversionQuantity
    if (quantityDiff < 0) throw NotEnoughException(
      listOf(Pair(product, MeasureValue(quantityDiff, measureValue.measure))))
    else return product.quantity - measureValue.double.toBase(measureValue.measure) / product.capacity
  }
}