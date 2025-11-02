package com.example.apptiendadeportiva_grupo10.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel(
    private val repo: ProductoRepository = ProductoRepository()
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun cargarProductos(context: Context) {
        viewModelScope.launch {
            _loading.value = true
            val list = repo.obtenerProductosDesdeAssets(context)
            _productos.value = list
            _loading.value = false
        }
    }

    fun buscarProductoPorId(id: Int): Producto? {
        return _productos.value.find { it.id == id }
    }
    fun reducirStock(productoId: Int, talla: String, cantidad: Int = 1): Boolean {
        // Usamos una copia de la lista para modificarla
        val listaActualizada = _productos.value.toMutableList()
        val index = listaActualizada.indexOfFirst { it.id == productoId }
        if (index == -1) return false
        val productoOriginal = listaActualizada[index]
        val stockActual = productoOriginal.stockPorTalla[talla] ?: 0
        if (stockActual < cantidad) return false
        val nuevoStockMap = productoOriginal.stockPorTalla.toMutableMap()
        val nuevoStock = stockActual - cantidad
        nuevoStockMap[talla] = nuevoStock
        val productoActualizado = productoOriginal.copy(stockPorTalla = nuevoStockMap)
        listaActualizada[index] = productoActualizado
        _productos.value = listaActualizada
        return true
    }
}