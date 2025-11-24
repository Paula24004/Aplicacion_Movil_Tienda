package com.example.apptiendadeportiva_grupo10.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.apptiendadeportiva_grupo10.data.Converters
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity

// FIX: Se incrementa la versión a 2 para resolver el error de "Room cannot verify the data integrity".
@Database(entities = [ProductoEntity::class], version = 2, exportSchema = false)
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
                )
                    // FIX: Se añade la opción para migración destructiva.
                    // Esto borrará la base de datos antigua y la recreará con el nuevo esquema si hay un error de integridad.
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}