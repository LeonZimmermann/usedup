package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.NotEnoughException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.MeasureValue
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.toBase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.toMeasure

class ConsumptionCalculator {
  fun calculateUpdatedQuantity(product: Product, measureValue: MeasureValue): Double {
    val existingQuantity = product.quantity * product.capacity
    val conversionQuantity = measureValue.double.toBase(measureValue.measure)
    val quantityDiff = existingQuantity - conversionQuantity
    if (quantityDiff < 0) throw NotEnoughException(product,
      MeasureValue(quantityDiff.toMeasure(measureValue.measure), measureValue.measure))
    else return product.quantity - measureValue.double.toBase(measureValue.measure) / product.capacity
  }
}