package com.example.apptiendadeportiva_grupo10.pruebasunitarias

import com.example.apptiendadeportiva_grupo10.api.ProductApiService
import com.example.apptiendadeportiva_grupo10.model.ProductoDto
import com.example.apptiendadeportiva_grupo10.repository.ProductoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ProductoRepositoryTest {

    private val api = mock(ProductApiService::class.java)
    private val repository = ProductoRepository(api)

    @Test
    fun `getProductosSoloAPI devuelve lista desde API`() = runBlocking {

        val productosApi = listOf(
            ProductoDto(
                id = 1,
                nombre = "Producto Test",
                descripcion = "Desc",
                precio = 1000.0,
                categoria = "Calzado",
                size = "M",
                color = "Rojo",
                imagenUrl = "img",
                stockPorTalla = mapOf("M" to 5)
            )
        )

        `when`(api.getProducts()).thenReturn(productosApi)

        val result = repository.getProductosSoloAPI()

        assertEquals(1, result.size)
        assertEquals("Producto Test", result.first().nombre)
    }
}
