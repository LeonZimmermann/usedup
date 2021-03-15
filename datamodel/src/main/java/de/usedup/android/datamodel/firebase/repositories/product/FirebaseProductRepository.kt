package de.usedup.android.datamodel.firebase.repositories.product

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Product
import de.usedup.android.datamodel.api.repositories.product.InMemoryQuantityProcessor
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import de.usedup.android.datamodel.firebase.filterForUser
import de.usedup.android.datamodel.firebase.objects.*
import de.usedup.android.datamodel.firebase.repositories.FirebaseUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException

object FirebaseProductRepository : ProductRepository {
  private val collection = Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME)

  private val consumptionDatabaseProcessor = FirebaseQuantityProcessor()
  private val consumptionInMemoryProcessor = InMemoryQuantityProcessor()

  override fun getAllProducts(): Set<Product> = runBlocking(Dispatchers.IO) {
    Tasks.await(collection.filterForUser().get()).map { Product.createInstance(it.id, it.toObject()) }.toSet()
  }

  override suspend fun getProductForId(id: Id) = withContext(Dispatchers.IO) {
    val document = Tasks.await(collection.document((id as FirebaseId).value).get())
    if (document.exists()) {
      val firebaseProduct = document.toObject<FirebaseProduct>() ?: throw IOException()
      Product.createInstance(document.id, firebaseProduct)
    } else {
      null
    }
  }

  override suspend fun getProductForName(name: String) = withContext(Dispatchers.IO) {
    val document = Tasks.await(collection.filterForUser().whereEqualTo("name", name).get()).first()
    if (document.exists()) {
      val firebaseProduct = document.toObject<FirebaseProduct>()
      Product.createInstance(document.id, firebaseProduct)
    } else {
      null
    }
  }

  override suspend fun addProduct(
    name: String,
    categoryId: Id,
    capacity: Double,
    measureId: Id,
    quantity: Double,
    min: Int,
    max: Int
  ): Unit = withContext(Dispatchers.IO) {
    val measureReference =
      Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).document((measureId as FirebaseId).value)
    val categoryReference =
      Firebase.firestore.collection(FirebaseCategory.COLLECTION_NAME).document((categoryId as FirebaseId).value)
    val firebaseProduct = FirebaseProduct(name, quantity, min, max, capacity, measureReference, categoryReference,
      FirebaseUserRepository.getDocumentReferenceToCurrentUser())
    val task = collection.add(firebaseProduct).apply { Tasks.await(this) }
    task.exception?.let { throw IOException(it) }
  }

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
      if (task.exception != null) throw IOException(requireNotNull(task.exception))
      else {
        getProductForId(id)?.apply {
          this.name = name
          this.categoryId = categoryId
          this.capacity = capacity
          this.measureId = measureId
          this.quantity = quantity
          this.min = min
          this.max = max
        }
      }
    }
  }

  override suspend fun changeQuantity(product: Product, updatedQuantity: Double) = withContext(Dispatchers.IO) {
    consumptionDatabaseProcessor.updateSingleProductQuantity(product, updatedQuantity)
    consumptionInMemoryProcessor.updateSingleProductQuantity(product, updatedQuantity)
  }

  override suspend fun changeQuantity(data: List<Pair<Product, Double>>) = withContext(Dispatchers.IO) {
    consumptionDatabaseProcessor.updateMultipleProductQuantities(data)
    consumptionInMemoryProcessor.updateMultipleProductQuantities(data)
  }

  override suspend fun deleteProduct(id: Id): Unit = withContext(Dispatchers.IO) {
    // TODO Warn user that meals and templates containing this product will be deleted
    // TODO Retry on failure
    val productReference = collection.document((id as FirebaseId).value)
    collection.document(id.value).delete().addOnSuccessListener {
      Firebase.firestore.collection(FirebaseTemplate.COLLECTION_NAME)
        .whereArrayContains("components", productReference)
        .addSnapshotListener { value, error ->
          if (error == null) {
            value?.documents?.map { it.reference }?.forEach { it.delete() }
          } else {
            // TODO Add logging and retry
          }
        }
      Firebase.firestore.collection(FirebaseMeal.COLLECTION_NAME)
        .whereArrayContains("ingredients", productReference)
        .addSnapshotListener { value, error ->
          if (error == null) {
            value?.documents?.map { it.reference }?.forEach { it.delete() }
          } else {
            // TODO Add logging and retry
          }
        }
    }

  }

}