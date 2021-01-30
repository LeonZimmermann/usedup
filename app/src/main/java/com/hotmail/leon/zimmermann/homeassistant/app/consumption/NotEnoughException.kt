package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import com.hotmail.leon.zimmermann.homeassistant.app.toFloatFormat
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.MeasureValue
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Product
import kotlin.math.absoluteValue

class NotEnoughException(product: Product, missingQuantity: MeasureValue) :
  ConsumptionException(
    "You are missing ${missingQuantity.double.absoluteValue.toFloatFormat()}${missingQuantity.measure.abbreviation} of ${product.name}")