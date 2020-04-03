package com.hotmail.leon.zimmermann.homeassistant.models.tables.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryEntity
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
            parentColumns = ["packaging_id"],
            childColumns = ["packaging_id"]
        ),
        ForeignKey(
            entity = MeasureEntity::class,
            parentColumns = ["measure_id"],
            childColumns = ["measure_id"]
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"]
        )
    ]
)
data class ProductEntity(
    var name: String,
    var quantity: Double,
    var min: Int,
    var max: Int,
    var capacity: Double,
    @ColumnInfo(name = "measure_id") var measureId: Long,
    @ColumnInfo(name = "category_id") var categoryId: Long,
    @ColumnInfo(name = "packaging_id") var packagingId: Long? = null
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    var id: Long = 0

    class ProductReductionException(message: String) : Exception(message)

    val discrepancy: Int
        get() = max(max - floor(quantity).toInt(), 0)

    // TODO Should not be part of the data model, as it is business logic
    fun reduce(value: Double, measure: Measure = Measure.values()[measureId.toInt()]) {
        val valueInBase = measure.toBaseMeasure(value)
        val reductionPercentage = valueInBase / capacity
        if (quantity < reductionPercentage) throw ProductReductionException(
            "Canceling transaction. Otherwise the amount " +
                    "would go below 0, which is impossible. Please manage the Product \"$name\" in the management view."
        )
        else quantity -= reductionPercentage
    }
}