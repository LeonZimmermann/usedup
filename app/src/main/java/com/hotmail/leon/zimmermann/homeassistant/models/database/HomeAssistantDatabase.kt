package com.hotmail.leon.zimmermann.homeassistant.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureDao
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.MeasureEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.packaging.PackagingDao
import com.hotmail.leon.zimmermann.homeassistant.models.tables.packaging.PackagingEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductDao
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.locks.ReentrantLock

@Database(
    entities = [ProductEntity::class, MeasureEntity::class, PackagingEntity::class],
    version = 1
)
abstract class HomeAssistantDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun measureDao(): MeasureDao
    abstract fun packagingDao(): PackagingDao

    private class HomeAssistantDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch { addMeasures(database) }
            }
        }

        private suspend fun addMeasures(database: HomeAssistantDatabase) {
            val dao = database.measureDao()
            for (measure in Measure.values())
                dao.insert(MeasureEntity(measure.ordinal, measure.text, measure.abbreviation))
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