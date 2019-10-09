package com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consumption_list")
class ConsumptionListMetaDataEntity(
    @PrimaryKey var id: Int,
    var name: String
)