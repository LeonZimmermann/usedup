package de.usedup.android.datamodel.firebase.repositories

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.usedup.android.datamodel.api.objects.Category
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.repositories.CategoryRepository
import de.usedup.android.datamodel.firebase.objects.FirebaseCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FirebaseCategoryRepository : CategoryRepository {
  override val categories = mutableListOf<Category>()

  override suspend fun init() {
    withContext(Dispatchers.IO) {
      categories.clear()
      Tasks.await(Firebase.firestore.collection(FirebaseCategory.COLLECTION_NAME).get()).forEach { document ->
        categories.add(Category.createInstance(document.id, document.toObject()))
      }
    }
  }

  override fun getCategoryForId(id: Id) = categories.firstOrNull { it.id == id }
  override fun getCategoryForName(name: String) = categories.firstOrNull { it.name == name }
}