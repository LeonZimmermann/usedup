package com.hotmail.leon.zimmermann.homeassistant.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hotmail.leon.zimmermann.homeassistant.models.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.measure.MeasureDao
import com.hotmail.leon.zimmermann.homeassistant.models.measure.MeasureEntity
import com.hotmail.leon.zimmermann.homeassistant.models.packaging.PackagingEntity
import com.hotmail.leon.zimmermann.homeassistant.models.product.ProductEntity
import com.hotmail.leon.zimmermann.homeassistant.models.product.ProductDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.locks.ReentrantLock

@Database(entities = [ProductEntity::class, MeasureEntity::class, PackagingEntity::class], version = 1)
abstract class HomeAssistantDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun measureDao(): MeasureDao

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
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(lock) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HomeAssistantDatabase::class.java,
                    "homeassistant-database"
                )
                    .addCallback(HomeAssistantDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}