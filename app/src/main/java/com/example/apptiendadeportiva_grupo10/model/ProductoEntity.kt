package com.example.apptiendadeportiva_grupo10.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey val id: Int,
    val nombre: String?,
    val descripcion: String?,
    val precio: Double?,
    val categoria: String?,
    val size: String?,
    val color: String?,
    val imagenUrl: String?,
    val stockPorTalla: Map<String, Int>?
)

object MapTypeConverter {
    @TypeConverter
    @JvmStatic
    fun fromMapToJson(map: Map<String, Int>?): String? {
        return Gson().toJson(map)
    }

    @TypeConverter
    @JvmStatic
    fun fromJsonToMap(json: String?): Map<String, Int>? {
        val type = object : TypeToken<Map<String, Int>>() {}.type
        return Gson().fromJson(json, type)
    }
}
