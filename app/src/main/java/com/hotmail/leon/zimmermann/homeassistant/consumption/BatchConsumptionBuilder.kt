package com.hotmail.leon.zimmermann.homeassistant.consumption

import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.ValueWithMeasure

class BatchConsumptionBuilder {
    private val resultHandlerList = mutableListOf<ResultHandler>()

    fun addResultHandler(resultHandler: ResultHandler): BatchConsumptionBuilder {
        resultHandlerList.add(resultHandler)
        return this
    }

    private fun notifyOnSuccess(batch: List<Pair<Product, Double>>) {
        resultHandlerList.forEach { it.onSuccess(batch) }
    }

    private fun notifyOnFailure(missingQuantities: List<Pair<Product, ValueWithMeasure>>) {
        resultHandlerList.forEach { it.onFailure(missingQuantities) }
    }

    fun consume(consumption: List<Pair<Product, ValueWithMeasure>>) {
        val batch = mutableListOf<Pair<Product, Double>>()
        val missingQuantities = mutableListOf<Pair<Product, ValueWithMeasure>>()
        consumption.forEach {
            val (product, value) = it
            SingleConsumptionBuilder().addResultHandler(object : SingleConsumptionBuilder.ResultHandler {
                override fun onSuccess(newQuantity: Pair<Product, Double>) {
                    batch.add(newQuantity)
                }

                override fun onFailure(missingQuantity: Pair<Product, ValueWithMeasure>) {
                    missingQuantities.add(missingQuantity)
                }
            }).consume(product, value)
        }
        if (missingQuantities.isEmpty()) notifyOnSuccess(batch)
        else notifyOnFailure(missingQuantities)
    }

    interface ResultHandler {
        fun onSuccess(batch: List<Pair<Product, Double>>) {}
        fun onFailure(missingQuantities: List<Pair<Product, ValueWithMeasure>>) {}
    }
}