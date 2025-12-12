package com.example.apptiendadeportiva_grupo10.pruebasunitarias

import com.example.apptiendadeportiva_grupo10.api.BoletaApi
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.repository.BoletaRepository
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class CarritoViewModelTest {

    private fun productoMock() = Producto(
        id = 1,
        nombre = "Zapatilla",
        descripcion = "Calzado deportivo",
        precio = 10000.0,
        categoria = "Calzado",
        size = "M",
        color = "Negro",
        imagenUrl = "url",
        stockPorTalla = mapOf("M" to 10)
    )

    @Test
    fun `agregar producto`() = runTest {

        val boletaApi = mock(BoletaApi::class.java)
        val boletaRepository = BoletaRepository(boletaApi)

        val vm = CarritoViewModel(boletaRepository)

        val p = productoMock()

        vm.agregar(p, "M", 1)
        advanceUntilIdle()

        assertEquals(1, vm.items.value.size)
        assertEquals(1, vm.items.value.first().cantidad)
    }

    @Test
    fun `cambiar cantidad`() = runTest {

        val boletaApi = mock(BoletaApi::class.java)
        val boletaRepository = BoletaRepository(boletaApi)

        val vm = CarritoViewModel(boletaRepository)
        val p = productoMock()

        vm.agregar(p, "M", 1)
        vm.cambiarCantidad(1, 5)
        advanceUntilIdle()

        assertEquals(5, vm.items.value.first().cantidad)
    }

    @Test
    fun `quitar producto`() = runTest {

        val boletaApi = mock(BoletaApi::class.java)
        val boletaRepository = BoletaRepository(boletaApi)

        val vm = CarritoViewModel(boletaRepository)
        val p = productoMock()

        vm.agregar(p, "M", 1)
        vm.quitar(1)
        advanceUntilIdle()

        assertEquals(0, vm.items.value.size)
    }
}
