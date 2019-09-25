package com.hotmail.leon.zimmermann.homeassistant.models.productmeasure

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductMeasureJoinDao {
    @Query(
        """
        SELECT * FROM measures
        INNER JOIN product_measure_join ON measures.id = product_measure_join.measure_id
        WHERE product_measure_join.product_id = :productId
    """
    )
    fun getMeasuresForProduct(productId: Int)

    @Insert
    suspend fun insert(productMeasureJoin: ProductMeasureJoin)
}