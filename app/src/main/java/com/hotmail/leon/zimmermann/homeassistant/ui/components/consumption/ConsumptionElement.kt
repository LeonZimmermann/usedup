package com.hotmail.leon.zimmermann.homeassistant.ui.components.consumption

import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Value

data class ConsumptionElement(val product: Product, var value: Value)