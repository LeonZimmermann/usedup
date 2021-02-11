package de.usedup.android.consumption

import de.usedup.android.datamodel.api.objects.*
import de.usedup.android.datamodel.api.repositories.MeasureRepository

class ConsumptionCalculator(private val measureRepository: MeasureRepository) {
  @Throws(NotEnoughException::class)
  fun calculateUpdatedQuantity(product: Product, measureValue: MeasureValue): Double {
    val productMeasure = measureRepository.getMeasureForId(product.measureId)
    if (productMeasure.type != measureValue.measure.type) {
      throw ConsumptionException("Incompatible measures")
    }
    return if (measureValue.measure.complex) {
      calculateUpdatedQuantityForComplexMode(product, productMeasure, measureValue)
    } else {
      calculateUpdatedQuantityForSimpleMode(product, measureValue)
    }
  }

  private fun calculateUpdatedQuantityForComplexMode(product: Product, productMeasure: Measure,
    measureValue: MeasureValue): Double {
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

  private fun calculateUpdatedQuantityForSimpleMode(product: Product,
    measureValue: MeasureValue): Double {
    val consumptionValue = measureValue.double.toInt()
    if (consumptionValue.toDouble() != measureValue.double) {
      throw ConsumptionException("The provided consumption needs to be an integer")
    }
    val quantityDiff = product.quantity - consumptionValue
    if (quantityDiff < 0) {
      throw NotEnoughException(product,
        MeasureValue(quantityDiff, measureValue.measure))
    } else {
      return quantityDiff
    }
  }
}