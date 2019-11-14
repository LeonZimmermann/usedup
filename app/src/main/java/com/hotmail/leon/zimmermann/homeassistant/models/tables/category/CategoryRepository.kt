package com.hotmail.leon.zimmermann.homeassistant.models.tables.category

class CategoryRepository(private val categoryDao: CategoryDao) {

    val categoryList = categoryDao.getAll()

    suspend fun insert(categoryEntity: CategoryEntity) {
        categoryDao.insert(categoryEntity)
    }
}