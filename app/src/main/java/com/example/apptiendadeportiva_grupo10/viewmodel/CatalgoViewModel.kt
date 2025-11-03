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
        val listaRaw = repo.obtenerProductosDesdeAssets(getApplication())


        val lista = listaRaw.map { p ->
            val stockNormalizado = p.stockPorTalla
                .mapKeys { (k, _) -> k.trim().uppercase() }
            p.copy(stockPorTalla = stockNormalizado)
        }

        _productos.value = lista
    }


    fun buscarProductoPorId(id: Int): Producto? =
        _productos.value.firstOrNull { it.id == id }

    private fun encontrarClaveTalla(p: Producto, talla: String): String? {
        val target = talla.trim()
        return p.stockPorTalla.keys.firstOrNull { it.equals(target, ignoreCase = true) }
    }


    fun reducirStock(idProducto: Int, talla: String, unidades: Int): Boolean {
        val lista = _productos.value
        val idx = lista.indexOfFirst { it.id == idProducto }
        if (idx == -1) return false

        val p = lista[idx]
        val claveReal = encontrarClaveTalla(p, talla) ?: return false

        val actual = p.stockPorTalla[claveReal] ?: 0
        if (actual < unidades) return false

        val nuevoStock = p.stockPorTalla.toMutableMap()
        nuevoStock[claveReal] = actual - unidades

        val actualizado = p.copy(stockPorTalla = nuevoStock)
        _productos.value = lista.toMutableList().apply { this[idx] = actualizado }
        return true
    }

    fun devolverStock(idProducto: Int, talla: String, unidades: Int) {
        val lista = _productos.value
        val idx = lista.indexOfFirst { it.id == idProducto }
        if (idx == -1) return

        val p = lista[idx]
        val claveReal = encontrarClaveTalla(p, talla) ?: return

        val actual = p.stockPorTalla[claveReal] ?: 0
        val nuevoStock = p.stockPorTalla.toMutableMap()
        nuevoStock[claveReal] = (actual + unidades).coerceAtLeast(0)

        val actualizado = p.copy(stockPorTalla = nuevoStock)
        _productos.value = lista.toMutableList().apply { this[idx] = actualizado }
    }

}

