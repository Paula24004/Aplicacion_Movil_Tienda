package com.example.apptiendadeportiva_grupo10.pruebasunitarias



import com.example.apptiendadeportiva_grupo10.model.ProductoEntity
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import org.junit.Assert.*
import org.junit.Test

class CatalogoViewModelTest {

    @Test
    fun `asignar productos manualmente al ViewModel`() {

        val vm = CatalogoViewModel()

        val productosMock = listOf(
            ProductoEntity(1, "Zapatilla", "Azul", 10000.0, "url1", null),
            ProductoEntity(2, "Polera", "Roja", 20000.0, "url2", null)
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
