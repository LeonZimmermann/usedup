package com.hotmail.leon.zimmermann.homeassistant.ui.components.consumption

import com.hotmail.leon.zimmermann.homeassistant.datamodel.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.Value

data class ConsumptionElement(val product: Product, var value: Value)