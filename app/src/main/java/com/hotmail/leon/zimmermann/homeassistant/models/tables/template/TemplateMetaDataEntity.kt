package com.hotmail.leon.zimmermann.homeassistant.models.tables.template

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "templates")
data class TemplateMetaDataEntity(
    var name: String
): Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}