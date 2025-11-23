package com.example.apptiendadeportiva_grupo10.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.apptiendadeportiva_grupo10.data.Converters
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity

@Database(entities = [ProductoEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "catalogo_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}