package com.example.apptiendadeportiva_grupo10.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity
import com.example.apptiendadeportiva_grupo10.model.MapTypeConverter

@Database(entities = [ProductoEntity::class], version = 3, exportSchema = false)
@TypeConverters(MapTypeConverter::class)
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
                )

                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}