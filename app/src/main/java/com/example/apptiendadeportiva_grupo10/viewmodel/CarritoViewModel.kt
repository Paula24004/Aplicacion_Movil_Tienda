package com.example.apptiendadeportiva_grupo10.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiendadeportiva_grupo10.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int
)

class CarritoViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val items: StateFlow<List<ItemCarrito>> = _items

    val total: StateFlow<Double> = _items
        .map { lista -> lista.sumOf { it.producto.precio * it.cantidad } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    fun agregar(producto: Producto, cantidad: Int = 1) {
        val actual = _items.value.toMutableList()
        val i = actual.indexOfFirst { it.producto.id == producto.id}
        if (i >= 0) {
            val previo = actual[i]
            actual[i] = previo.copy(cantidad = (previo.cantidad + cantidad).coerceAtLeast(1))
        } else {
            actual.add(ItemCarrito(producto, cantidad))
        }
        _items.value = actual
    }

    fun cambiarCantidad(idProducto: Int, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            quitar(idProducto)
        } else {
            _items.value = _items.value.map {
                if (it.producto.id == idProducto) it.copy(cantidad = nuevaCantidad) else it
            }
        }
    }

    fun quitar(idProducto: Int) {
        _items.value = _items.value.filterNot { it.producto.id == idProducto }
    }

    fun vaciar() { _items.value = emptyList() }
}
