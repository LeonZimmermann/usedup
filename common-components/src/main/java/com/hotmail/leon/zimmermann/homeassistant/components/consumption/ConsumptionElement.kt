package com.hotmail.leon.zimmermann.homeassistant.components.consumption

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.MeasureValue

data class ConsumptionElement(val product: Product, var valueValue: MeasureValue)