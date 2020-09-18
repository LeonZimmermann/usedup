package com.hotmail.leon.zimmermann.homeassistant.components.consumption

import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.ValueWithMeasure

data class ConsumptionElement(val product: Product, var value: ValueWithMeasure)