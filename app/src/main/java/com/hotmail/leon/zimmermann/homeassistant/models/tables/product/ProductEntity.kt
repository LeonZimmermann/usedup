package com.hotmail.leon.zimmermann.homeassistant.models.tables.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.packaging.PackagingEntity
import kotlin.math.ceil
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
    @ColumnInfo(name = "measure_id") var measureId: Int,
    @ColumnInfo(name = "packaging_id") var packagingId: Int? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    class ProductReductionException(message: String): Exception(message)

    val discrepancy: Int
        get() = max(max - floor(quantity).toInt(), 0)

    fun reduce(value: Double, measure: Measure = Measure.values()[measureId]) {
        val valueInBase = measure.toBaseMeasure(value)
        val reductionPercentage = valueInBase / capacity
        if (quantity < reductionPercentage) throw ProductReductionException("Canceling transaction. Otherwise the quantity" +
                "would go below 0, which is impossible. Please manage the Product \"$name\" in the management view.")
        else quantity -= reductionPercentage
    }
}