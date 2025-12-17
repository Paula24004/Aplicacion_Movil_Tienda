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

    fun getProductos(context: Context): Flow<List<ProductoEntity>> = flow {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()

        try {
            val dtoList = api.getProducts()
            val entities = dtoList.map { it.toEntity() }

            dao.insertAll(entities)
            emit(entities)
        } catch (e: Exception) {
            e.printStackTrace()
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

    suspend fun insertProducto(context: Context, producto: Producto): Result<Unit> {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()

        return try {
            // Log para verificar el tamaño antes de disparar la petición
            val base64Length = producto.imagenUrl?.length ?: 0
            android.util.Log.d("REPO_DEBUG", "Enviando imagen de tamaño: $base64Length caracteres")

            val response = api.createProduct(producto.toDto())

            if (response.isSuccessful && response.body() != null) {
                val productoEntity = response.body()!!.toEntity()
                dao.insert(productoEntity)
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                // Si el error contiene "Large" o "413", es por el tamaño de la imagen en application.properties
                Result.failure(Exception("Servidor rechazó la petición: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }

    suspend fun updateProducto(context: Context, producto: Producto): Result<Unit> {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()

        // El ID del producto NO debe ser nulo o 0 para actualizar
        if (producto.id == 0) {
            return Result.failure(Exception("El ID del producto es inválido (0) para la modificación."))
        }

        val productoDtoParaEnvio = producto.toDto()

        return try {
            // 1. Llamada a la API de modificación
            val response = api.updateProduct(producto.id, productoDtoParaEnvio)

            if (response.isSuccessful && response.body() != null) {
                // 2. ÉXITO: Guarda la respuesta actualizada en Room.
                //    ProductoDao.insert con OnConflictStrategy.REPLACE maneja la actualización.
                val productoDtoActualizado = response.body()!!
                val productoEntity = productoDtoActualizado.toEntity()
                dao.insert(productoEntity)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al modificar producto en el API: ${response.code()}. Respuesta: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red al modificar producto: ${e.message}"))
        }
    }

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

