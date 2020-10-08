package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Category
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id

interface CategoryRepository {
  fun init()
  val categories: MutableList<Category>
  fun getCategoryForId(id: Id): Category
  fun getCategoryForName(name: String): Category
}