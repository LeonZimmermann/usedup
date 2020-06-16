package com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions

import com.hotmail.leon.zimmermann.homeassistant.datamodel.Value
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product

class NotEnoughException(missingQuantity: List<Pair<Product, Value>>) :
    ConsumptionException("Missing:\n" + missingQuantity.map { "${it.second.double}${it.second.measure.abbreviation} of ${it.first.name}" }
        .reduce { acc, s -> "$acc\n$s" }) {
}