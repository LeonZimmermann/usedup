package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*

class SingleConsumptionBuilder {
    private val resultHandlerList = mutableListOf<ResultHandler>()

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

    fun consume(product: Product, valueValue: MeasureValue) {
        val existingQuantity = product.quantity * product.capacity
        val conversionQuantity = valueValue.double.toBase(valueValue.measure)
        val quantityDiff = existingQuantity - conversionQuantity
        if (quantityDiff < 0) notifyOnFailure(Pair(product, MeasureValue(quantityDiff.toMeasure(valueValue.measure), valueValue.measure)))
        else {
            val newQuantity = product.quantity - valueValue.double.toBase(valueValue.measure) / product.capacity
            notifyOnSuccess(Pair(product, newQuantity))
        }
    }

    interface ResultHandler {
        fun onSuccess(newQuantity: Pair<Product, Double>) {}
        fun onFailure(missingQuantity: Pair<Product, MeasureValue>) {}
    }
}