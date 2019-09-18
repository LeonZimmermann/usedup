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

    companion object {
        @Volatile
        private var INSTANCE: ProductDatabase? = null
        private var lock = ReentrantLock()

        fun getDatabase(context: Context, scope: CoroutineScope): ProductDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(lock) {
                val instance = Room.databaseBuilder(context.applicationContext, ProductDatabase::class.java, "homeassistant")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}