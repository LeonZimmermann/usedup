package com.hotmail.leon.zimmermann.homeassistant.app.shopping

import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Meal
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.toMeasure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository
import kotlin.math.ceil

class ShoppingListBuilder {
    private val list = mutableListOf<ShoppingProduct>()

    fun addDiscrepancies(): ShoppingListBuilder {
        ProductRepository.products
            .filter { it.discrepancy > 0 }
            .forEach { addProduct(it, it.discrepancy) }
        return this
    }

    fun addProduct(product: Product, cartAmount: Int): ShoppingListBuilder {
        val productInList = list.firstOrNull { it.product == product }
        if (productInList == null) list.add(ShoppingProduct(product, cartAmount))
        else productInList.cartAmount += cartAmount
        return this
    }

    fun addMeal(meal: Meal): ShoppingListBuilder {
        meal.ingredients.forEach {
            val product = ProductRepository.getProductForId(it.productId)
            val measure = MeasureRepository.getMeasureForId(it.measureId)
            val cartAmount = ceil(product.capacity.toMeasure(measure) / it.value).toInt()
            addProduct(product, cartAmount)
        }
        return this
    }

    fun build(): List<ShoppingProduct> {
        return list
    }

}