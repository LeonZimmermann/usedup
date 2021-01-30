package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.MeasureValue
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.toBase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.toMeasure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MeasureRepository

class ConsumptionCalculator(private val measureRepository: MeasureRepository) {
  @Throws(NotEnoughException::class)
  fun calculateUpdatedQuantity(product: Product, measureValue: MeasureValue): Double {
    val productMeasure = measureRepository.getMeasureForId(product.measureId)
    if (productMeasure.type != measureValue.measure.type) {
      throw ConsumptionException("Incompatible measures")
    }
    val existingQuantity = product.quantity * product.capacity
    val existingValueAsBase = existingQuantity.toBase(productMeasure)
    val consumptionValueAsBase = measureValue.double.toBase(measureValue.measure)
    val quantityDiff = existingValueAsBase - consumptionValueAsBase
    if (quantityDiff < 0) {
      throw NotEnoughException(product,
        MeasureValue(quantityDiff.toMeasure(measureValue.measure), measureValue.measure))
    } else {
      return (existingValueAsBase - consumptionValueAsBase).toMeasure(productMeasure) / product.capacity
    }
  }
}