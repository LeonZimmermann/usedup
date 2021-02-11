package de.usedup.android.components.consumption

import de.usedup.android.datamodel.api.objects.Product
import de.usedup.android.datamodel.api.objects.MeasureValue

data class ConsumptionElement(val product: Product, var valueValue: MeasureValue)