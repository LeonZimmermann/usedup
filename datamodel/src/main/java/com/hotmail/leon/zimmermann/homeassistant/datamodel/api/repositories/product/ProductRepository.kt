package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product

import androidx.lifecycle.MutableLiveData
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Product
import java.io.IOException

interface ProductRepository {

  val products: MutableLiveData<MutableList<Product>>

  suspend fun init()

  fun getAllProducts(): List<Product>

  @Throws(NoSuchElementException::class)
  suspend fun getProductForId(id: Id): Product

  @Throws(NoSuchElementException::class)
  suspend fun getProductForName(name: String): Product

  @Throws(IOException::class)
  suspend fun addProduct(name: String, categoryId: Id, capacity: Double, measureId: Id, quantity: Double,
    min: Int, max: Int)

  @Throws(IOException::class, NoSuchElementException::class)
  suspend fun updateProduct(id: Id, name: String, categoryId: Id, capacity: Double, measureId: Id,
    quantity: Double, min: Int, max: Int)

  @Throws(IOException::class)
  suspend fun changeQuantity(product: Product, updatedQuantity: Double)

  @Throws(IOException::class)
  suspend fun changeQuantity(data: List<Pair<Product, Double>>)

  @Throws(IOException::class, NoSuchElementException::class)
  suspend fun deleteProduct(id: Id)

}