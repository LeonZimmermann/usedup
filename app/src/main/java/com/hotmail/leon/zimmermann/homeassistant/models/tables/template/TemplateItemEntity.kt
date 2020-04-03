package com.hotmail.leon.zimmermann.homeassistant.models.tables.template

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity

@Entity(
    tableName = "template_items",
    primaryKeys = ["template_id", "product_id"],
    foreignKeys = [
        ForeignKey(
            entity = MeasureEntity::class,
            parentColumns = ["measure_id"],
            childColumns = ["measure_id"]
        )
    ]
)
data class TemplateItemEntity(
    @ColumnInfo(name = "template_id") var templateId: Long,
    @ColumnInfo(name = "product_id") var productId: Long,
    var value: Double,
    @ColumnInfo(name = "measure_id") var measureId: Long
)