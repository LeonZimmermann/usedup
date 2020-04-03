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
import com.hotmail.leon.zimmermann.homeassistant.models.tables.meal.*
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureDao
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.packaging.PackagingDao
import com.hotmail.leon.zimmermann.homeassistant.models.tables.packaging.PackagingEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductDao
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.template.Template
import com.hotmail.leon.zimmermann.homeassistant.models.tables.template.TemplateDao
import com.hotmail.leon.zimmermann.homeassistant.models.tables.template.TemplateItemEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.template.TemplateMetaDataEntity
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
        MealIngredientEntity::class,
        MealEntity::class,
        TemplateItemEntity::class,
        TemplateMetaDataEntity::class,
        CategoryEntity::class,
        CalendarActivityEntity::class,
        DinnerActivityDetailsEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class HomeAssistantDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun measureDao(): MeasureDao
    abstract fun packagingDao(): PackagingDao
    abstract fun mealDao(): MealDao
    abstract fun templateDao(): TemplateDao
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
                    addMockMeals(database)
                    //addMockTemplates(database)
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
                val measure = random.nextInt(Measure.values().size).toLong()
                val category = random.nextInt(Category.values().size).toLong()
                dao.insert(ProductEntity(name, quantity, min, max, capacity, measure, category))
            }
        }

        private suspend fun addMockMeals(database: HomeAssistantDatabase) {
            val mealDao = database.mealDao()
            val products = database.productDao().getAllStatically()
            val measures = database.measureDao().getAllStatically()
            val names = listOf("Meal1", "Meal2", "Meal3", "Meal4")
            val random = Random()
            for (name in names) {
                mealDao.insert(Meal(name, random.nextInt(60), null, null, null, List(products.size) {
                    MealIngredient(
                        products[it].id,
                        random.nextDouble(),
                        measures[random.nextInt(measures.size)].id
                    )
                }))
            }
        }

        private suspend fun addMockTemplates(database: HomeAssistantDatabase) {
            val templateDao = database.templateDao()
            val products = database.productDao().getAllStatically()
            val measures = database.measureDao().getAllStatically()
            val names = listOf("Template1", "Template2", "Template3", "Template4")
            val random = Random()
            for (name in names) {
                templateDao.insert(
                    Template(
                        TemplateMetaDataEntity(name),
                        List(products.size) {
                            TemplateItemEntity(
                                products[it].id.toInt(),
                                measures[random.nextInt(measures.size)].id.toInt(),
                                random.nextDouble()
                            )
                        })
                )
            }
        }

        private suspend fun addMockCalendarEntries(database: HomeAssistantDatabase) {
            val dao = database.calendarDao()
            val consumptionDao = database.mealDao()
            val random = Random()
            dao.insertDinnerActivityDetailsList(consumptionDao.getAll().value!!.map { DinnerActivityDetailsEntity(it.meal.id) })
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