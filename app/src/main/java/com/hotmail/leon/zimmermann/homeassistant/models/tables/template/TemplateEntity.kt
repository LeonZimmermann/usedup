package com.hotmail.leon.zimmermann.homeassistant.models.tables.template

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "templates")
data class TemplateEntity(
    var name: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "template_id")
    var id: Long = 0
}