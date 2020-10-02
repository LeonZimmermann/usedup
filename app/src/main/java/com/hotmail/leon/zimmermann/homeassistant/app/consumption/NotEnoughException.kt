package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.MeasureValue
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import kotlin.math.absoluteValue

class NotEnoughException(product: Product, missingQuantity: MeasureValue) :
  ConsumptionException(
    "You are missing ${missingQuantity.double.absoluteValue}${missingQuantity.measure.abbreviation} of ${product.name}")