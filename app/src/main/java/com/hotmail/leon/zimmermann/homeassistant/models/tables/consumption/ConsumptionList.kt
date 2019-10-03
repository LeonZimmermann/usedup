package com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption

import androidx.room.Ignore
import androidx.room.Relation

class ConsumptionList(
    val name: String,
    consumptionData: List<ConsumptionData>
) {
    @Relation(parentColumn = "id", entityColumn = "list_id")
    val consumptions: List<ConsumptionEntity> = consumptionData.map {
        ConsumptionEntity(it.product.id, it.measure.id, it.value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConsumptionList

        if (name != other.name) return false
        if (consumptions != other.consumptions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + consumptions.hashCode()
        return result
    }

    override fun toString(): String {
        return "ConsumptionList(name='$name', consumptionData=$consumptions)"
    }
}