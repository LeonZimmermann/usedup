package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseCategory
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseMeasure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException

object ProductRepository {
  private val database = Firebase.firestore
  private val consumptionDatabaseProcessor = QuantityDatabaseProcessor()
  private val consumptionInMemoryProcessor = QuantityInMemoryProcessor()

  val products: MutableLiveData<MutableList<Product>> = MutableLiveData()

  init {
    database.collection(FirebaseProduct.COLLECTION_NAME).get().addOnSuccessListener { documents ->
      products.value = documents.map { Product.createInstance(it.id, it.toObject()) }.toMutableList()
    }
  }

  fun getAllProducts(): List<Product> = runBlocking(Dispatchers.IO) {
    if (products.value != null) products.value!!
    else Tasks.await(database.collection(FirebaseProduct.COLLECTION_NAME).get())
      .map { Product.createInstance(it.id, it.toObject()) }
  }

  @Throws(NoSuchElementException::class)
  suspend fun getProductForId(id: String): Product = withContext(Dispatchers.IO) {
    if (products.value != null) products.value!!.first { it.id == id }
    else {
      val document = Tasks.await(database.collection(FirebaseProduct.COLLECTION_NAME).document(id).get())
      val firebaseProduct = document.toObject<FirebaseProduct>() ?: throw IOException()
      val product = Product.createInstance(document.id, firebaseProduct)
      val productList = products.value!!
      productList.add(product)
      products.postValue(productList)
      product
    }
  }

  @Throws(NoSuchElementException::class)
  suspend fun getProductForName(name: String): Product = withContext(Dispatchers.IO) {
    if (products.value != null) products.value!!.first { it.name == name }
    else {
      val document =
        Tasks.await(database.collection(FirebaseProduct.COLLECTION_NAME).whereEqualTo("name", name).get()).first()
      val firebaseProduct = document.toObject<FirebaseProduct>()
      val product = Product.createInstance(document.id, firebaseProduct)
      val productList = products.value!!
      productList.add(product)
      products.postValue(productList)
      product
    }
  }

  @Throws(IOException::class)
  suspend fun addProduct(
    name: String,
    categoryId: String,
    capacity: Double,
    measureId: String,
    quantity: Double,
    min: Int,
    max: Int
  ) = withContext(Dispatchers.IO) {
    val measureReference = database.collection(FirebaseMeasure.COLLECTION_NAME).document(measureId)
    val categoryReference = database.collection(FirebaseCategory.COLLECTION_NAME).document(categoryId)
    val product = mapOf(
      "name" to name, "quantity" to quantity, "min" to min, "max" to max, "capacity" to capacity,
      "measureReference" to measureReference,
      "categoryReference" to categoryReference
    )
    val task = database.collection(FirebaseProduct.COLLECTION_NAME).add(product)
    Tasks.await(task)
    if (task.exception != null) throw IOException(task.exception!!)
    else {
      val productList = products.value!!
      productList.add(
        Product.createInstance(
          task.result!!.id,
          FirebaseProduct(name, quantity, min, max, capacity, measureReference, categoryReference)
        )
      )
      products.postValue(productList)
    }
  }

  @Throws(IOException::class)
  suspend fun updateProduct(
    id: String,
    name: String,
    categoryId: String,
    capacity: Double,
    measureId: String,
    quantity: Double,
    min: Int,
    max: Int
  ) = withContext(Dispatchers.IO) {
    val measureReference = database.collection(FirebaseMeasure.COLLECTION_NAME).document(measureId)
    val categoryReference = database.collection(FirebaseCategory.COLLECTION_NAME).document(categoryId)
    val data = mapOf(
      "name" to name, "quantity" to quantity, "min" to min, "max" to max, "capacity" to capacity,
      "measureReference" to measureReference,
      "categoryReference" to categoryReference
    )
    val task = database.collection(FirebaseProduct.COLLECTION_NAME).document(id).update(data)
    Tasks.await(task)
    if (task.exception != null) throw IOException(task.exception!!)
    else {
      getProductForId(id).apply {
        this.name = name
        this.categoryId = categoryId
        this.capacity = capacity
        this.measureId = measureId
        this.quantity = quantity
        this.min = min
        this.max = max
      }
      products.postValue(products.value)
    }
  }

  @Throws(IOException::class)
  suspend fun changeQuantity(product: Product, updatedQuantity: Double) = withContext(Dispatchers.IO) {
    consumptionDatabaseProcessor.updateSingleProductQuantity(product, updatedQuantity)
    consumptionInMemoryProcessor.updateSingleProductQuantity(product, updatedQuantity)
    products.postValue(products.value)
  }

  @Throws(IOException::class)
  suspend fun changeQuantity(data: List<Pair<Product, Double>>) = withContext(Dispatchers.IO) {
    consumptionDatabaseProcessor.updateMultipleProductQuantities(data)
    consumptionInMemoryProcessor.updateMultipleProductQuantities(data)
    products.postValue(products.value)
  }

  @Throws(IOException::class, NoSuchElementException::class)
  suspend fun deleteProduct(productId: String) = withContext(Dispatchers.IO) {
    // TODO Account for changes in templates and meals
    products.value!!.remove(getProductForId(productId))
    val task = database.collection(FirebaseProduct.COLLECTION_NAME).document(productId).delete()
    Tasks.await(task)
    if (task.exception != null) throw IOException(task.exception!!)
    else products.postValue(products.value)
  }

}