package com.example.apptiendadeportiva_grupo10.repository

import android.content.Context
import com.example.apptiendadeportiva_grupo10.data.local.AppDatabase
import com.example.apptiendadeportiva_grupo10.data.remote.ApiService
import com.example.apptiendadeportiva_grupo10.data.remote.RetrofitClient
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity
import com.example.apptiendadeportiva_grupo10.model.toEntity

class ProductoRepository(
    private val api: ApiService = RetrofitClient.apiService   // inyección opcional
) {

    suspend fun getProductos(context: Context): List<ProductoEntity> {

        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()

        return try {
            val dtoList = api.getProducts()       // ← YA USAMOS EL API INYECTADO
            val entities = dtoList.map { it.toEntity() }

            dao.insertAll(entities)
            entities

        } catch (e: Exception) {
            e.printStackTrace()
            dao.getAll()
        }
    }

    suspend fun getProductoPorId(context: Context, id: Int): ProductoEntity? {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()

        val local = dao.findById(id)
        if (local != null) return local

        return try {
            val dto = api.getProducts().find { it.id == id }
            dto?.toEntity()?.also { dao.insertAll(listOf(it)) }
        } catch (_: Exception) {
            null
        }
    }
}
