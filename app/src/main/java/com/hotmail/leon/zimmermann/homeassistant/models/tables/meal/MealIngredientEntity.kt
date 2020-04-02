package com.hotmail.leon.zimmermann.homeassistant.models.tables.meal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity

@Entity(
    tableName = "meal_ingredients",
    primaryKeys = ["list_id", "product_id"],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"]
        ),
        ForeignKey(
            entity = MeasureEntity::class,
            parentColumns = ["id"],
            childColumns = ["measure_id"]
        )
    ]
)
data class MealIngredientEntity(
    @ColumnInfo(name = "product_id") var productId: Int,
    @ColumnInfo(name = "measure_id") var measureId: Int,
    var value: Double
) {
    @ColumnInfo(name = "list_id") var listId: Int = 0
}