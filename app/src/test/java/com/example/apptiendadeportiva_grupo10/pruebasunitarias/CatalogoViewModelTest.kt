package com.example.apptiendadeportiva_grupo10.pruebasunitarias

import com.example.apptiendadeportiva_grupo10.model.ProductoEntity
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import org.junit.Assert.*
import org.junit.Test

class CatalogoViewModelTest {

    private fun productoMock(
        id: Int,
        nombre: String
    ) = ProductoEntity(
        id = id,
        nombre = nombre,
        descripcion = "Descripción",
        precio = 10000.0,
        categoria = "Calzado",
        size = "M",
        color = "Azul",
        imagenUrl = "url",
        stockPorTalla = mapOf("M" to 10)
    )

    @Test
    fun `asignar productos manualmente al ViewModel`() {
        val vm = CatalogoViewModel()

        val productosMock = listOf(
            productoMock(1, "Zapatilla"),
            productoMock(2, "Polera")
        )

        vm.setProductosForTest(productosMock)

        assertEquals(2, vm.productos.value.size)
        assertEquals("Zapatilla", vm.productos.value.first().nombre)
    }

    @Test
    fun `asignar lista vacia al catalogo`() {
        val vm = CatalogoViewModel()
        vm.setProductosForTest(emptyList())
        assertEquals(0, vm.productos.value.size)
    }
}
