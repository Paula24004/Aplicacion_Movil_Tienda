package com.example.apptiendadeportiva_grupo10.repository

import android.content.Context
import com.example.apptiendadeportiva_grupo10.data.local.AppDatabase
import com.example.apptiendadeportiva_grupo10.data.remote.RetrofitClient
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity
import com.example.apptiendadeportiva_grupo10.model.toEntity
import com.example.apptiendadeportiva_grupo10.model.toDto
import com.example.apptiendadeportiva_grupo10.api.ProductApiService

class ProductoRepository(
    private val api: ProductApiService = RetrofitClient.apiService
) {

    suspend fun getProductos(context: Context): List<ProductoEntity> {

        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()

        return try {
            val dtoList = api.getProducts()
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

    /**
     * Inserta un nuevo producto: 1. API (POST), 2. Room (Local)
     */
    suspend fun insertProducto(context: Context, producto: Producto) {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()

        val productoDtoParaEnvio = producto.toDto()

        try {
            val response = api.createProduct(productoDtoParaEnvio)

            if (response.isSuccessful && response.body() != null) {
                // ÉXITO: Guarda en Room
                val productoDtoCreado = response.body()!!
                val productoEntity = productoDtoCreado.toEntity()
                dao.insert(productoEntity)

            } else {
                // 🚨 PUNTO CLAVE: Esto imprimirá el error 400/500 si ocurre.
                println("Error al crear producto en el API: ${response.code()}. Respuesta: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            // 🚨 PUNTO CLAVE: Esto imprimirá si falla la conexión de red.
            println("Error de red al crear producto: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun deleteProducto(context: Context, id: Int) {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()
        dao.deleteById(id)
    }
}