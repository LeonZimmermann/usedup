package com.hotmail.leon.zimmermann.homeassistant.models.product

class ProductRepository(private val productDao: ProductDao) {
    val productList = productDao.getAll()

    suspend fun insert(productEntity: ProductEntity) {
        productDao.insert(productEntity)
    }

    suspend fun insertAll(productEntityList: List<ProductEntity>) {
        productDao.insertAll(productEntityList)
    }

    suspend fun update(productEntity: ProductEntity) {
        productDao.update(productEntity)
    }

    suspend fun updateAll(productEntityList: List<ProductEntity>) {
        productDao.updateAll(productEntityList)
    }


    suspend fun delete(productEntity: ProductEntity) {
        productDao.delete(productEntity)
    }

    suspend fun deleteAll() {
        productDao.deleteAll()
    }
}