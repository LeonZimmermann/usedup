package com.hotmail.leon.zimmermann.homeassistant.product

class ProductRepository(private val productDao: ProductDao) {
    val productList = productDao.getAll()

    suspend fun insert(product: Product) {
        productDao.insert(product)
    }

    suspend fun insertAll(productList: List<Product>) {
        productDao.insertAll(productList)
    }

    suspend fun update(product: Product) {
        productDao.update(product)
    }

    suspend fun delete(product: Product) {
        productDao.delete(product)
    }

    suspend fun deleteAll() {
        productDao.deleteAll()
    }
}