package com.example.apptiendadeportiva_grupo10.pruebasunitarias



import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import org.junit.Assert.*
import org.junit.Test

class CarritoViewModelTest {

    private fun productoMock() = Producto(
        id = 1,
        nombre = "Zapatilla",
        descripcion = "Calzado deportivo",
        precio = 10000.0,
        imagenUrl = "url",
        stockPorTalla = mapOf("35-38" to 10)
    )

    @Test
    fun `agregar producto`() {
        val vm = CarritoViewModel()
        val p = productoMock()

        vm.agregar(p, "35-38", 1)

        assertEquals(1, vm.items.value.size)
        assertEquals(1, vm.items.value.first().cantidad)
    }

    @Test
    fun `cambiar cantidad`() {
        val vm = CarritoViewModel()
        val p = productoMock()

        vm.agregar(p, "35-38", 1)
        vm.cambiarCantidad(1, 5)

        assertEquals(5, vm.items.value.first().cantidad)
    }

    @Test
    fun `calcular total`() {
        val vm = CarritoViewModel()
        val p = productoMock()

        vm.agregar(p, "35-38", 2)

        assertEquals(20000.0, vm.total.value, 0.1)
    }

    @Test
    fun `quitar producto`() {
        val vm = CarritoViewModel()
        val p = productoMock()

        vm.agregar(p, "35-38", 1)
        vm.quitar(1)

        assertEquals(0, vm.items.value.size)
    }
}
