package com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consumption_list")
class ConsumptionListMetaDataEntity(var name: String) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}