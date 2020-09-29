package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.ConsumptionException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*

class SingleConsumptionBuilder {
  private val resultHandlerList = mutableListOf<ResultHandler>()
  private val processor = ConsumptionProcessor()

  fun addResultHandler(resultHandler: ResultHandler): SingleConsumptionBuilder {
    resultHandlerList.add(resultHandler)
    return this
  }

  private fun notifyOnSuccess(newQuantity: Pair<Product, Double>) {
    resultHandlerList.forEach { it.onSuccess(newQuantity) }
  }

  private fun notifyOnFailure(missingQuantity: Pair<Product, MeasureValue>) {
    resultHandlerList.forEach { it.onFailure(missingQuantity) }
  }

  fun consume(product: Product, measureValue: MeasureValue) {
    try {
      val newQuantity = processor.consume(product, measureValue)
      notifyOnSuccess(Pair(product, newQuantity))
    } catch (e: ConsumptionException) {
      notifyOnFailure(
        Pair(product, MeasureValue(processor.quantityDiff.toMeasure(measureValue.measure), measureValue.measure)))
    }
  }

  interface ResultHandler {
    fun onSuccess(newQuantity: Pair<Product, Double>) {}
    fun onFailure(missingQuantity: Pair<Product, MeasureValue>) {}
  }
}