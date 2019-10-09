package com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption

import androidx.room.Embedded
import androidx.room.Relation

class ConsumptionList(
    @Embedded
    var metaData: ConsumptionListMetaDataEntity,
    @Relation(parentColumn = "id", entityColumn = "list_id", entity = ConsumptionEntity::class)
    var consumptions: List<ConsumptionEntity>
)