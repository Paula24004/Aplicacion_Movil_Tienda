package com.example.apptiendadeportiva_grupo10.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Clase que contiene los métodos de conversión para tipos complejos
// Room usará estos métodos para convertir tipos no primitivos (Map, List) a String (JSON)
// y viceversa, para poder guardarlos en la base de datos SQLite.
class Converters {

    // ----------------------------------------------------
    // CONVERSOR 1: MAPA DE STRING A INT (Map<String, Int>)
    // Resuelve el problema con 'stockPorTalla'.
    // ----------------------------------------------------

    /**
     * Convierte una cadena JSON almacenada en la base de datos a un Map<String, Int>.
     * Se usa al leer los datos de la BD.
     */
    @TypeConverter
    fun fromStockMap(value: String?): Map<String, Int> {
        // Devuelve un mapa vacío si el valor es nulo o vacío.
        if (value.isNullOrBlank()) {
            return emptyMap()
        }
        val mapType = object : TypeToken<Map<String, Int>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    /**
     * Convierte un Map<String, Int> a una cadena JSON para ser guardada en la base de datos.
     * Se usa al escribir los datos en la BD.
     */
    @TypeConverter
    fun toStockMap(map: Map<String, Int>): String {
        return Gson().toJson(map)
    }

    // ----------------------------------------------------
    // CONVERSOR 2: LISTA DE STRINGS (List<String>)
    // ----------------------------------------------------

    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        if (value == null) return null
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String? {
        if (list == null) return null
        return Gson().toJson(list)
    }

    // ----------------------------------------------------
    // CONVERSOR 3: LISTA DE ENTEROS (List<Int>)
    // ----------------------------------------------------

    @TypeConverter
    fun fromIntList(value: String?): List<Int>? {
        if (value == null) return null
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toIntList(list: List<Int>?): String? {
        if (list == null) return null
        return Gson().toJson(list)
    }
}