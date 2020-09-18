package com.hotmail.leon.zimmermann.homeassistant.consumption

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

    private fun notifyOnFailure(missingQuantity: Pair<Product, ValueWithMeasure>) {
        resultHandlerList.forEach { it.onFailure(missingQuantity) }
    }

    fun consume(product: Product, value: ValueWithMeasure) {
        val existingQuantity = product.quantity * product.capacity
        val conversionQuantity = value.double.toBase(value.measure)
        val quantityDiff = existingQuantity - conversionQuantity
        if (quantityDiff < 0) notifyOnFailure(Pair(product, ValueWithMeasure(quantityDiff.toMeasure(value.measure), value.measure)))
        else {
            val newQuantity = product.quantity - value.double.toBase(value.measure) / product.capacity
            notifyOnSuccess(Pair(product, newQuantity))
        }
    }

    interface ResultHandler {
        fun onSuccess(newQuantity: Pair<Product, Double>) {}
        fun onFailure(missingQuantity: Pair<Product, ValueWithMeasure>) {}
    }
}