package com.hotmail.leon.zimmermann.homeassistant.models.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hotmail.leon.zimmermann.homeassistant.models.packaging.PackagingEntity
import kotlin.math.max

@Entity(
    tableName = "products", foreignKeys = [ForeignKey(
        entity = PackagingEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("packaging_id")
    )]
)
data class ProductEntity(
    var name: String,
    var quantity: Int,
    var min: Int,
    var max: Int,
    var capacity: Double,
    @ColumnInfo(name = "packaging_id") var packagingId: Int? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    val discrepancy: Int
        get() = max(max - quantity, 0)

    // TODO Add method reduce
}