package com.hotmail.leon.zimmermann.homeassistant.models.tables.category

class CategoryRepository(private val categoryDao: CategoryDao) {

    val categoryList = categoryDao.getAll()

    suspend fun getAllStatically(): List<CategoryEntity> = categoryDao.getAllStatically()

    suspend fun insert(categoryEntity: CategoryEntity) {
        categoryDao.insert(categoryEntity)
    }

    suspend fun update(categoryList: List<CategoryEntity>) {
        categoryDao.update(categoryList)
    }
}