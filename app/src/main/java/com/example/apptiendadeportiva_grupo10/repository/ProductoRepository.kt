package com.example.apptiendadeportiva_grupo10.repository

import android.content.Context
import com.example.apptiendadeportiva_grupo10.data.local.AppDatabase
import com.example.apptiendadeportiva_grupo10.data.remote.RetrofitClient
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity
import com.example.apptiendadeportiva_grupo10.model.toEntity
import com.example.apptiendadeportiva_grupo10.model.toDto
import com.example.apptiendadeportiva_grupo10.api.ProductApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductoRepository(
    private val api: ProductApiService = RetrofitClient.apiService
) {

    // Cambiado a Flow para compatibilidad con ViewModel (emite lista de ProductoEntity)
    fun getProductos(context: Context): Flow<List<ProductoEntity>> = flow {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()

        try {
            val dtoList = api.getProducts()
            val entities = dtoList.map { it.toEntity() }

            dao.insertAll(entities)  // Actualiza cache en Room
            emit(entities)  // Emite desde API
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback a Room si API falla
            emit(dao.getAll())
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
     * Cambiado a Result<Unit> para manejar éxito/error
     */
    suspend fun insertProducto(context: Context, producto: Producto): Result<Unit> {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()

        val productoDtoParaEnvio = producto.toDto()

        return try {
            val response = api.createProduct(productoDtoParaEnvio)

            if (response.isSuccessful && response.body() != null) {
                // ÉXITO: Guarda en Room
                val productoDtoCreado = response.body()!!
                val productoEntity = productoDtoCreado.toEntity()
                dao.insert(productoEntity)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al crear producto en el API: ${response.code()}. Respuesta: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red al crear producto: ${e.message}"))
        }
    }

    /**
     * Elimina un producto: 1. API (DELETE), 2. Room (Local)
     * Cambiado a Result<Unit> para manejar éxito/error
     */
    suspend fun deleteProducto(context: Context, id: Int): Result<Unit> {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()

        return try {
            val response = api.deleteProduct(id)  // ¡AGREGADO! Llama a DELETE en API

            if (response.isSuccessful) {
                // ÉXITO: Elimina de Room
                dao.deleteById(id)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar producto en el API: ${response.code()}. Respuesta: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red al eliminar producto: ${e.message}"))
        }
    }

    suspend fun getProductosSoloAPI(): List<ProductoEntity> {
        val dtoList = api.getProducts()
        return dtoList.map { it.toEntity() }
    }
}

