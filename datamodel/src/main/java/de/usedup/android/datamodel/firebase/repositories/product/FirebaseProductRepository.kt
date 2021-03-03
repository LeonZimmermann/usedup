package de.usedup.android.datamodel.firebase.repositories.product

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Product
import de.usedup.android.datamodel.api.repositories.product.InMemoryQuantityProcessor
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import de.usedup.android.datamodel.firebase.filterForUser
import de.usedup.android.datamodel.firebase.objects.FirebaseCategory
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import de.usedup.android.datamodel.firebase.objects.FirebaseMeasure
import de.usedup.android.datamodel.firebase.objects.FirebaseProduct
import de.usedup.android.datamodel.firebase.repositories.FirebaseUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException

object FirebaseProductRepository : ProductRepository {
  private val collection = Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME)

  private val consumptionDatabaseProcessor = FirebaseQuantityProcessor()
  private val consumptionInMemoryProcessor = InMemoryQuantityProcessor()

  override val products: MutableLiveData<MutableList<Product>> = MutableLiveData()

  override suspend fun init() {
    withContext(Dispatchers.IO) {
      collection.filterForUser().get()
        .addOnSuccessListener { documents ->
          products.value = documents.map { Product.createInstance(it.id, it.toObject()) }.toMutableList()
        }
    }
  }

  override fun getAllProducts(): List<Product> = runBlocking(Dispatchers.IO) {
    if (products.value != null) products.value!!
    else Tasks.await(collection.filterForUser().get()).map { Product.createInstance(it.id, it.toObject()) }
  }

  override suspend fun getProductForId(id: Id) = withContext(Dispatchers.IO) {
    if (products.value != null) products.value?.firstOrNull { it.id == id }
    else {
      val document = Tasks.await(collection.document((id as FirebaseId).value).get())
      val firebaseProduct = document.toObject<FirebaseProduct>() ?: throw IOException()
      val product = Product.createInstance(document.id, firebaseProduct)
      val productList = products.value!!
      productList.add(product)
      products.postValue(productList)
      product
    }
  }

  override suspend fun getProductForName(name: String) = withContext(Dispatchers.IO) {
    if (products.value != null) products.value?.first { it.name == name }
    else {
      val document = Tasks.await(collection.filterForUser().whereEqualTo("name", name).get()).first()
      val firebaseProduct = document.toObject<FirebaseProduct>()
      val product = Product.createInstance(document.id, firebaseProduct)
      val productList = products.value!!
      productList.add(product)
      products.postValue(productList)
      product
    }
  }

  @Throws(IOException::class)
  override suspend fun addProduct(
    name: String,
    categoryId: Id,
    capacity: Double,
    measureId: Id,
    quantity: Double,
    min: Int,
    max: Int
  ) = withContext(Dispatchers.IO) {
    val measureReference =
      Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).document((measureId as FirebaseId).value)
    val categoryReference =
      Firebase.firestore.collection(FirebaseCategory.COLLECTION_NAME).document((categoryId as FirebaseId).value)
    val firebaseProduct = FirebaseProduct(name, quantity, min, max, capacity, measureReference, categoryReference,
      FirebaseUserRepository.getDocumentReferenceToCurrentUser())
    val task = collection.add(firebaseProduct).apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception!!)
    else {
      val productList = products.value!!
      productList.add(Product.createInstance(task.result!!.id, firebaseProduct))
      products.postValue(productList)
    }
  }

  @Throws(IOException::class)
  override suspend fun updateProduct(id: Id, name: String, categoryId: Id, capacity: Double, measureId: Id,
    quantity: Double, min: Int, max: Int) {
    withContext(Dispatchers.IO) {
      val measureReference =
        Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).document((measureId as FirebaseId).value)
      val categoryReference =
        Firebase.firestore.collection(FirebaseCategory.COLLECTION_NAME).document((categoryId as FirebaseId).value)
      val data = mapOf(
        "name" to name, "quantity" to quantity, "min" to min, "max" to max, "capacity" to capacity,
        "measureReference" to measureReference,
        "categoryReference" to categoryReference
      )
      val task = collection.document((id as FirebaseId).value).update(data).apply { Tasks.await(this) }
      if (task.exception != null) throw IOException(task.exception!!)
      else {
        getProductForId(id)?.apply {
          this.name = name
          this.categoryId = categoryId
          this.capacity = capacity
          this.measureId = measureId
          this.quantity = quantity
          this.min = min
          this.max = max
          products.postValue(products.value)
        }
      }
    }
  }

  @Throws(IOException::class)
  override suspend fun changeQuantity(product: Product, updatedQuantity: Double) = withContext(Dispatchers.IO) {
    consumptionDatabaseProcessor.updateSingleProductQuantity(product, updatedQuantity)
    consumptionInMemoryProcessor.updateSingleProductQuantity(product, updatedQuantity)
    products.postValue(products.value)
  }

  @Throws(IOException::class)
  override suspend fun changeQuantity(data: List<Pair<Product, Double>>) = withContext(Dispatchers.IO) {
    consumptionDatabaseProcessor.updateMultipleProductQuantities(data)
    consumptionInMemoryProcessor.updateMultipleProductQuantities(data)
    products.postValue(products.value)
  }

  @Throws(IOException::class)
  override suspend fun deleteProduct(id: Id) = withContext(Dispatchers.IO) {
    // TODO Account for changes in templates and meals
    requireNotNull(products.value).remove(getProductForId(id))
    val task = collection.document((id as FirebaseId).value).delete().apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception!!)
    else products.postValue(products.value)
  }

}