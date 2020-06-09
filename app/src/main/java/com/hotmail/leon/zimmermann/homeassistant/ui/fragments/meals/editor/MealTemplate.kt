package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.editor

import com.hotmail.leon.zimmermann.homeassistant.datamodel.Measure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.Value

data class MealTemplate(val product: Product, var value: Value)