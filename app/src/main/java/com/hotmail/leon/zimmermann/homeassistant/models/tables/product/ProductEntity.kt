package com.hotmail.leon.zimmermann.homeassistant.models.tables.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.packaging.PackagingEntity
import kotlin.math.floor
import kotlin.math.max

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = PackagingEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("packaging_id")
        ),
        ForeignKey(
            entity = MeasureEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("measure_id")
        )
    ]
)
data class ProductEntity(
    var name: String,
    var quantity: Double,
    var min: Int,
    var max: Int,
    var capacity: Double,
    @ColumnInfo(name = "packaging_id") var packagingId: Int? = null,
    @ColumnInfo(name = "measure_id") var measureId: Int? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    val discrepancy: Int
        get() = max(max - floor(quantity).toInt(), 0)

    // TODO Add method reduce
}