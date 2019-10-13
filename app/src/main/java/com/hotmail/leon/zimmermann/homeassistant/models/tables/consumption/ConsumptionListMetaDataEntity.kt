package com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consumption_list")
class ConsumptionListMetaDataEntity(
    var name: String,
    var duration: Int? = null,
    var description: String? = null,
    var instructions: String? = null,
    var backgroundUrl: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}