package com.hotmail.leon.zimmermann.homeassistant.models.productmeasure

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.hotmail.leon.zimmermann.homeassistant.models.measure.MeasureEntity
import com.hotmail.leon.zimmermann.homeassistant.models.product.ProductEntity

@Entity(
    tableName = "product_measure_join",
    primaryKeys = ["product_id", "measure_id"],
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("product_id")
    ), ForeignKey(
        entity = MeasureEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("measure_id")
    )]
)
data class ProductMeasureJoin(
    @ColumnInfo(name = "product_id") val productId: Int,
    @ColumnInfo(name = "measure_id") val measureId: Int
)