package de.usedup.android.datamodel.api.repositories

import de.usedup.android.datamodel.api.objects.Category
import de.usedup.android.datamodel.api.objects.Id

interface CategoryRepository {
  val categories: MutableList<Category>
  suspend fun init()
  fun getCategoryForId(id: Id): Category
  fun getCategoryForName(name: String): Category
}