package com.example.apptiendadeportiva_grupo10.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.model.BoletaDto
import com.example.apptiendadeportiva_grupo10.repository.BoletaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// --------------------
// MODELO ITEM CARRITO
// --------------------
data class ItemCarrito(
    val producto: Producto,
    val talla: String,
    val cantidad: Int
)

// --------------------
// VIEWMODEL
// --------------------
class CarritoViewModel(
    private val boletaRepository: BoletaRepository
) : ViewModel() {

    // Lista de items del carrito
    private val _items = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val items: StateFlow<List<ItemCarrito>> = _items

    // Total calculado automáticamente
    val total: StateFlow<Double> = _items
        .map { lista ->
            lista.sumOf { it.producto.precio.toDouble() * it.cantidad }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0.0
        )

    // --------------------
    // STOCK (opcional)
    // --------------------
    private val _stockInicial = MutableStateFlow<Map<Int, Map<String, Int>>>(emptyMap())
    private var inicializado = false

    fun initStock(productos: List<Producto>) {
        if (inicializado || productos.isEmpty()) return

        _stockInicial.value = productos.associate { producto ->
            producto.id to (producto.stockPorTalla ?: emptyMap())
        }

        inicializado = true
    }

    // --------------------
    // OPERACIONES CARRITO
    // --------------------
    fun agregar(producto: Producto, talla: String, cantidad: Int = 1) {
        val actual = _items.value.toMutableList()

        val index = actual.indexOfFirst {
            it.producto.id == producto.id && it.talla == talla
        }

        if (index >= 0) {
            val previo = actual[index]
            actual[index] = previo.copy(cantidad = previo.cantidad + cantidad)
        } else {
            actual.add(ItemCarrito(producto, talla, cantidad))
        }

        _items.value = actual
    }

    fun cambiarCantidad(idProducto: Int, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            quitar(idProducto)
            return
        }

        _items.value = _items.value.map {
            if (it.producto.id == idProducto) it.copy(cantidad = nuevaCantidad)
            else it
        }
    }

    fun quitar(idProducto: Int) {
        _items.value = _items.value.filterNot { it.producto.id == idProducto }
    }

    fun vaciar() {
        _items.value = emptyList()
    }

    // --------------------
    // BOLETAS (BACKEND)
    // --------------------
    fun registrarBoleta(
        idProduct: Int,
        cantidad: Int,
        precio: Int
    ) {
        viewModelScope.launch {
            try {
                val boleta = BoletaDto(
                    cantidad = cantidad,
                    precio = precio,
                    idProduct = idProduct
                )
                boletaRepository.crearBoleta(boleta)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
