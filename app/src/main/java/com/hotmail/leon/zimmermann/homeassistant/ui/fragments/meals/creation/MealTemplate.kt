package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.creation

import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity

data class MealTemplate(val product: ProductEntity, val measure: Measure, val value: Double)