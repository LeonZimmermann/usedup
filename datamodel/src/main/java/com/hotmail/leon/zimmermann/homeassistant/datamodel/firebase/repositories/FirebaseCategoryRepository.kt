package com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Category
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.CategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FirebaseCategoryRepository : CategoryRepository {
  override val categories = mutableListOf<Category>()

  override suspend fun init() {
    withContext(Dispatchers.IO) {
      Tasks.await(Firebase.firestore.collection(FirebaseCategory.COLLECTION_NAME).get()).forEach { document ->
        categories.add(Category.createInstance(document.id, document.toObject()))
      }
    }
  }

  override fun getCategoryForId(id: Id) = categories.first { it.id == id }
  override fun getCategoryForName(name: String) = categories.first { it.name == name }
}