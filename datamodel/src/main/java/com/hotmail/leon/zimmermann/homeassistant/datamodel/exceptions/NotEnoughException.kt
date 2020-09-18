package com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions

import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.ValueWithMeasure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product

class NotEnoughException(missingQuantity: List<Pair<Product, ValueWithMeasure>>) :
    ConsumptionException("Missing:\n" + missingQuantity.map { "${it.second.double}${it.second.measure.abbreviation} of ${it.first.name}" }
        .reduce { acc, s -> "$acc\n$s" }) {
}