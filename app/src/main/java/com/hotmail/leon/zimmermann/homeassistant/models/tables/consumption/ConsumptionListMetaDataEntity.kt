package com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "consumption_list")
data class ConsumptionListMetaDataEntity(
    var name: String,
    var duration: Int? = null,
    var description: String? = null,
    var instructions: String? = null,
    var backgroundUrl: String? = null
): Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}