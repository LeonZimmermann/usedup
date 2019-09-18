package com.hotmail.leon.zimmermann.homeassistant.product

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.locks.ReentrantLock

@Database(entities = [Product::class], version = 1)
abstract class ProductDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao

    private class ProductDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.productDao())
                }
            }
        }

        suspend fun populateDatabase(productDao: ProductDao) {
            productDao.deleteAll()
            productDao.insert(Product("Cheese", 2, 2, 2))
            productDao.insert(Product("Salami", 1, 2, 2))
            productDao.insert(Product("Toast", 2, 1, 2))
            productDao.insert(Product("Butter", 0, 1, 3))
            productDao.insert(Product("Bodywash", 0, 1, 2))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ProductDatabase? = null
        private var lock = ReentrantLock()

        fun getDatabase(context: Context, scope: CoroutineScope): ProductDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(lock) {
                val instance = Room.databaseBuilder(context.applicationContext, ProductDatabase::class.java, "homeassistant")
                    .addCallback(ProductDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}