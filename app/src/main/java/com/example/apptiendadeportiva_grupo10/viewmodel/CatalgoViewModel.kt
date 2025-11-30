package com.example.apptiendadeportiva_grupo10.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity
import com.example.apptiendadeportiva_grupo10.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class CatalogoViewModel(
    private val repo: ProductoRepository = ProductoRepository()
) : ViewModel() {

    private val _productos = MutableStateFlow<List<ProductoEntity>>(emptyList())
    val productos: StateFlow<List<ProductoEntity>> = _productos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarProductos(context: Context) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                repo.getProductos(context).collect { lista ->
                    _productos.value = lista
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _loading.value = false
            }
        }
    }

    suspend fun getProductoById(context: Context, id: Int): ProductoEntity? {
        return withContext(Dispatchers.IO) {
            repo.getProductoPorId(context, id)
        }
    }
    // --- SOLO PARA TESTS ---
    fun setProductosForTest(lista: List<ProductoEntity>) {
        _productos.value = lista
    }

}