package de.usedup.android.datamodel.api.repositories.product

import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Product
import java.io.IOException

interface ProductRepository {

  fun getAllProducts(): Set<Product>

  suspend fun getProductForId(id: Id): Product?

  suspend fun getProductForName(name: String): Product?

  suspend fun addProduct(name: String, categoryId: Id, capacity: Double, measureId: Id, quantity: Double,
    min: Int, max: Int)

  suspend fun updateProduct(id: Id, name: String, categoryId: Id, capacity: Double, measureId: Id,
    quantity: Double, min: Int, max: Int)

  suspend fun changeQuantity(product: Product, updatedQuantity: Double)

  suspend fun changeQuantity(data: List<Pair<Product, Double>>)

  suspend fun deleteProduct(id: Id)

}