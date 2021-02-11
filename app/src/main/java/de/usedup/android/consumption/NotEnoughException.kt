package de.usedup.android.consumption

import de.usedup.android.utils.toFloatFormat
import de.usedup.android.datamodel.api.objects.MeasureValue
import de.usedup.android.datamodel.api.objects.Product
import kotlin.math.absoluteValue

class NotEnoughException(product: Product, missingQuantity: MeasureValue) :
  ConsumptionException(
    "You are missing ${missingQuantity.double.absoluteValue.toFloatFormat()}${missingQuantity.measure.abbreviation} of ${product.name}")