package com.example.apptiendadeportiva_grupo10.repository

import android.content.Context
import com.example.apptiendadeportiva_grupo10.data.local.AppDatabase
import com.example.apptiendadeportiva_grupo10.data.remote.RetrofitClient
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity
import com.example.apptiendadeportiva_grupo10.model.toEntity

class ProductoRepository {
    // Intenta traer datos desde la API; en caso de error devuelve datos de Room
    suspend fun getProductos(context: Context): List<ProductoEntity> {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()
        return try {
            val dtoList = RetrofitClient.apiService.getProducts()
            val entities = dtoList.map { it.toEntity() }
            // Insertar/actualizar cache
            dao.insertAll(entities)
            entities
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: datos en local (puede ser empty)
            dao.getAll()
        }
    }

    suspend fun getProductoPorId(context: Context, id: Int): ProductoEntity? {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()
        val fromDb = dao.findById(id)
        if (fromDb != null) return fromDb
        // Si no está en DB intentamos API individualmente (opcional)
        return try {
            val dto = RetrofitClient.apiService.getProducts().find { it.id == id }
            dto?.toEntity()?.also { dao.insertAll(listOf(it)) }
        } catch (_: Exception) {
            null
        }
    }
}