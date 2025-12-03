package com.example.apptiendadeportiva_grupo10.pruebasunitarias

import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CarritoViewModelTest {

    private fun productoMock() = Producto(
        id = 1,
        nombre = "Zapatilla",
        descripcion = "Calzado deportivo",
        precio = 10000.0,
        categoria = "poleras",
        size = "S",
        color = "verde",
        imagenUrl = "url",
        stockPorTalla = mapOf("35-38" to 10)
    )

    @Test
    fun `agregar producto`() = runTest {
        val vm = CarritoViewModel()
        val p = productoMock()

        vm.agregar(p, "35-38", 1)
        advanceUntilIdle()

        assertEquals(1, vm.items.value.size)
        assertEquals(1, vm.items.value.first().cantidad)
    }

    @Test
    fun `cambiar cantidad`() = runTest {
        val vm = CarritoViewModel()
        val p = productoMock()

        vm.agregar(p, "35-38", 1)
        advanceUntilIdle()

        vm.cambiarCantidad(1, 5)
        advanceUntilIdle()

        assertEquals(5, vm.items.value.first().cantidad)
    }



    @Test
    fun `quitar producto`() = runTest {
        val vm = CarritoViewModel()
        val p = productoMock()

        vm.agregar(p, "35-38", 1)
        advanceUntilIdle()

        vm.quitar(1)
        advanceUntilIdle()

        assertEquals(0, vm.items.value.size)
    }
}
