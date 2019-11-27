package com.hotmail.leon.zimmermann.homeassistant.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.Category
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryDao
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionDao
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionListMetaDataEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureDao
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.packaging.PackagingDao
import com.hotmail.leon.zimmermann.homeassistant.models.tables.packaging.PackagingEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductDao
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.locks.ReentrantLock

@Database(
    entities = [
        ProductEntity::class,
        MeasureEntity::class,
        PackagingEntity::class,
        ConsumptionEntity::class,
        ConsumptionListMetaDataEntity::class,
        CategoryEntity::class
    ],
    version = 1
)
abstract class HomeAssistantDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun measureDao(): MeasureDao
    abstract fun packagingDao(): PackagingDao
    abstract fun consumptionDao(): ConsumptionDao
    abstract fun categoryDao(): CategoryDao

    private class HomeAssistantDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    addMeasures(database)
                    addCategories(database)
                    addMockProducts(database)
                }
            }
        }

        private suspend fun addMeasures(database: HomeAssistantDatabase) {
            val dao = database.measureDao()
            for (measure in Measure.values())
                dao.insert(MeasureEntity(measure.id, measure.text, measure.abbreviation))
        }

        private suspend fun addCategories(database: HomeAssistantDatabase) {
            val dao = database.categoryDao()
            for ((index, category) in Category.values().withIndex())
                dao.insert(CategoryEntity(category.id, category.name, index))
        }

        private suspend fun addMockProducts(database: HomeAssistantDatabase) {
            val dao = database.productDao()
            val productList =
                listOf("Bodywash", "Shampoo", "Toothpaste", "Toilet Paper", "Toast", "Bread", "Salami", "Ham", "Cheese")
            val random = Random()
            for (name in productList) {
                val min = random.nextInt(2) + 1
                val max = min + random.nextInt(2)
                val quantity = 0.0
                val capacity = random.nextDouble()
                val measure = random.nextInt(Measure.values().size)
                val category = random.nextInt(Category.values().size)
                dao.insert(ProductEntity(name, quantity, min, max, capacity, measure, category))
            }
        }

        companion object {
            private const val TAG = "HADatabaseCallback"
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: HomeAssistantDatabase? = null
        private var lock = ReentrantLock()

        fun getDatabase(context: Context, scope: CoroutineScope): HomeAssistantDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(lock) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HomeAssistantDatabase::class.java,
                    "homeassistant-database"
                )
                    .addCallback(
                        HomeAssistantDatabaseCallback(
                            scope
                        )
                    )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}