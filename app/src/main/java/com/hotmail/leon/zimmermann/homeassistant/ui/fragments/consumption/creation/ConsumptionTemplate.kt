package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.creation

import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity

data class ConsumptionTemplate(val product: ProductEntity, val measure: Measure, val value: Double)