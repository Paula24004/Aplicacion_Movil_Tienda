package com.example.apptiendadeportiva_grupo10.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ProductoRepository()

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    init {
        cargar()
    }

    fun cargar() = viewModelScope.launch {
        val lista = repo.obtenerProductosDesdeAssets(getApplication())
        _productos.value = lista
    }

    fun buscarProductoPorId(id: Int): Producto? =
        _productos.value.firstOrNull { it.id == id }

    fun reducirStock(idProducto: Int, talla: String, unidades: Int): Boolean {
        val lista = _productos.value
        val idx = lista.indexOfFirst { it.id == idProducto }
        if (idx == -1) return false

        val p = lista[idx]
        val tallaKey = talla.trim().uppercase()
        val actual = p.stockPorTalla[tallaKey] ?: 0
        if (actual < unidades) return false

        val nuevoStock = p.stockPorTalla.toMutableMap()
        nuevoStock[tallaKey] = actual - unidades

        val actualizado = p.copy(stockPorTalla = nuevoStock)
        val nuevaLista = lista.toMutableList().apply { this[idx] = actualizado }
        _productos.value = nuevaLista
        return true
    }

    fun devolverStock(idProducto: Int, talla: String, unidades: Int) {
        val lista = _productos.value
        val idx = lista.indexOfFirst { it.id == idProducto }
        if (idx == -1) return

        val p = lista[idx]
        val tallaKey = talla.trim().uppercase()
        val actual = p.stockPorTalla[tallaKey] ?: 0

        val nuevoStock = p.stockPorTalla.toMutableMap()
        nuevoStock[tallaKey] = (actual + unidades).coerceAtLeast(0)

        val actualizado = p.copy(stockPorTalla = nuevoStock)
        val nuevaLista = lista.toMutableList().apply { this[idx] = actualizado }
        _productos.value = nuevaLista
    }
}
