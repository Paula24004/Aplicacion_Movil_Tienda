package com.example.apptiendadeportiva_grupo10.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.apptiendadeportiva_grupo10.model.Producto
class ProductoRepository {

    fun obtenerProductosDesdeAssets(context: Context, filename: String = "productos.json"): List<Producto> {
        return try {
            val json = context.assets.open(filename).bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<Producto>>() {}.type
            val productos = Gson().fromJson<List<Producto>>(json, listType)
            Log.d("ProductoRepository", "Productos cargados exitosamente: ${productos?.size ?: 0}")
            productos ?: emptyList()

        } catch (e: Exception) {
            Log.e("ProductoRepository", "FALLO al cargar productos desde assets. Causa: ${e.message}")
            e.printStackTrace()
            return emptyList()
        }
    }
}