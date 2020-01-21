package com.hotmail.leon.zimmermann.homeassistant.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar.*
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.Category
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryDao
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionDao
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionList
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
import java.sql.Date
import java.util.*
import java.util.concurrent.locks.ReentrantLock

@Database(
    entities = [
        ProductEntity::class,
        MeasureEntity::class,
        PackagingEntity::class,
        ConsumptionEntity::class,
        ConsumptionListMetaDataEntity::class,
        CategoryEntity::class,
        CalendarActivityEntity::class,
        CookingActivityDetailsEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class HomeAssistantDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun measureDao(): MeasureDao
    abstract fun packagingDao(): PackagingDao
    abstract fun consumptionDao(): ConsumptionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun calendarDao(): CalendarDao

    private class HomeAssistantDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    addMeasures(database)
                    addCategories(database)
                    addMockProducts(database)
                    addMockDinners(database)
                    addMockCalendarEntries(database)
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

        private suspend fun addMockDinners(database: HomeAssistantDatabase) {
            val consumptionDao = database.consumptionDao()
            val products = database.productDao().getAllStatically()
            val measures = database.measureDao().getAllStatically()
            val names = listOf("sgjr", "sghsrh", "drhdrhdr", "shdrhdt")
            val random = Random()
            for (i in 1..5) {
                consumptionDao.insert(
                    ConsumptionList(ConsumptionListMetaDataEntity(
                        names[random.nextInt(names.size)],
                        random.nextInt(60)
                    ),
                        List(products.size) {
                            ConsumptionEntity(
                                products[it].id,
                                measures[random.nextInt(measures.size)].id,
                                random.nextDouble()
                            )
                        })
                )
            }
        }

        private suspend fun addMockCalendarEntries(database: HomeAssistantDatabase) {
            val dao = database.calendarDao()
            val consumptionDao = database.consumptionDao()
            val random = Random()
            dao.insertCookingActivityDetails(consumptionDao.getAll().value!!.map { CookingActivityDetailsEntity(it.metaData.id) })
            dao.insertCalendarActivities(List(25) {
                val dateFrom = Date(Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, random.nextInt(15))
                    set(Calendar.HOUR, random.nextInt(12))
                    set(Calendar.MINUTE, random.nextInt(60))
                }.timeInMillis)
                val dateTo = Date(Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, random.nextInt(15))
                    set(Calendar.HOUR, random.nextInt(12))
                    set(Calendar.MINUTE, random.nextInt(60))
                }.timeInMillis)
                val typeId = random.nextInt(CalendarActivityType.values().size)
                val details = when (CalendarActivityType.values()[typeId]) {
                    CalendarActivityType.COOKING -> dao.getAllCookingActivityDetails().value!!
                    else -> null
                }
                val detailsId = if (details != null) details[random.nextInt(details.size)].id else null
                CalendarActivityEntity(dateFrom, dateTo, typeId, detailsId)
            })
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
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(lock) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HomeAssistantDatabase::class.java,
                    "homeassistant-database"
                ).addCallback(HomeAssistantDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}