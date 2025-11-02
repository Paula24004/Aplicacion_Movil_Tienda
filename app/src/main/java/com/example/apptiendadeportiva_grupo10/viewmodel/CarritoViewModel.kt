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
    val talla: String,
    val cantidad: Int
)

class CarritoViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val items: StateFlow<List<ItemCarrito>> = _items

    val total: StateFlow<Int> = _items
        .map { lista -> lista.sumOf { it.producto.precio * it.cantidad } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    private val _stock = MutableStateFlow<Map<Int, MutableMap<String, Int>>>(emptyMap())
    val stock: StateFlow<Map<Int, Map<String, Int>>> = _stock
        .map { outer -> outer.mapValues { it.value.toMap() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    fun initStock(productos: List<Producto>) {
        if (_stock.value.isNotEmpty()) return
        val base = productos.associate { p ->
            val porTalla: MutableMap<String, Int> =
                try {
                    @Suppress("UNCHECKED_CAST")
                    (Producto::class.java.getDeclaredField("stock")
                        .apply { isAccessible = true }
                        .get(p) as? Map<String, Int>)?.toMutableMap() ?: mutableMapOf()
                } catch (_: Exception) {
                    mutableMapOf()
                }
            p.id to porTalla
        }
        _stock.value = base
    }

    fun stockDe(productoId: Int, talla: String): Int =
        _stock.value[productoId]?.get(talla) ?: 0

    private fun setStock(productoId: Int, talla: String, nuevo: Int) {
        val outer = _stock.value.toMutableMap()
        val inner = outer[productoId]?.toMutableMap() ?: mutableMapOf()
        inner[talla] = nuevo.coerceAtLeast(0)
        outer[productoId] = inner
        _stock.value = outer
    }

    private fun decrementarSiDisponible(productoId: Int, talla: String): Boolean {
        val actual = stockDe(productoId, talla)
        return if (actual > 0) { setStock(productoId, talla, actual - 1); true } else false
    }

    private fun incrementar(productoId: Int, talla: String, cantidad: Int) {
        val actual = stockDe(productoId, talla)
        setStock(productoId, talla, actual + cantidad)
    }

    fun agregar(producto: Producto, talla: String, cantidad: Int = 1) {
        repeat(cantidad) {
            if (!decrementarSiDisponible(producto.id, talla)) return
        }
        val actual = _items.value.toMutableList()
        val i = actual.indexOfFirst { it.producto.id == producto.id && it.talla == talla }
        if (i >= 0) {
            val previo = actual[i]
            actual[i] = previo.copy(cantidad = (previo.cantidad + cantidad).coerceAtLeast(1))
        } else {
            actual.add(ItemCarrito(producto, talla, cantidad))
        }
        _items.value = actual
    }

    fun cambiarCantidad(idProducto: Int, talla: String, nuevaCantidad: Int) {
        val actual = _items.value.toMutableList()
        val idx = actual.indexOfFirst { it.producto.id == idProducto && it.talla == talla }
        if (idx < 0) return
        val item = actual[idx]
        if (nuevaCantidad <= 0) {
            incrementar(idProducto, talla, item.cantidad)
            actual.removeAt(idx)
        } else {
            val delta = nuevaCantidad - item.cantidad
            if (delta > 0) {
                repeat(delta) {
                    if (!decrementarSiDisponible(idProducto, talla)) return@repeat
                    actual[idx] = actual[idx].copy(cantidad = actual[idx].cantidad + 1)
                }
            } else if (delta < 0) {
                incrementar(idProducto, talla, -delta)
                actual[idx] = actual[idx].copy(cantidad = nuevaCantidad)
            }
        }
        _items.value = actual
    }

    fun cambiarCantidad(idProducto: Int, nuevaCantidad: Int) {
        val porProducto = _items.value.filter { it.producto.id == idProducto }
        porProducto.forEach { item ->
            cambiarCantidad(idProducto, item.talla, nuevaCantidad)
        }
    }

    fun quitar(idProducto: Int, talla: String) {
        val actual = _items.value.toMutableList()
        val idx = actual.indexOfFirst { it.producto.id == idProducto && it.talla == talla }
        if (idx >= 0) {
            val item = actual[idx]
            incrementar(idProducto, talla, item.cantidad)
            actual.removeAt(idx)
            _items.value = actual
        }
    }

    fun quitar(idProducto: Int) {
        val actual = _items.value.toMutableList()
        val quitar = actual.filter { it.producto.id == idProducto }
        quitar.forEach { incrementar(idProducto, it.talla, it.cantidad) }
        _items.value = actual.filterNot { it.producto.id == idProducto }
    }

    fun vaciar() {
        _items.value.forEach { incrementar(it.producto.id, it.talla, it.cantidad) }
        _items.value = emptyList()
    }
}
