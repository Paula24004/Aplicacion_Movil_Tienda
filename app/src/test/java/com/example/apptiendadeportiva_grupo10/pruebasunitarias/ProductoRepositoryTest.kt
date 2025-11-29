package com.example.apptiendadeportiva_grupo10.pruebasunitarias

import com.example.apptiendadeportiva_grupo10.model.ProductoDto
import com.example.apptiendadeportiva_grupo10.repository.ProductoRepository
import com.example.apptiendadeportiva_grupo10.api.ProductApiService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ProductoRepositoryTest {

    private val api = mock(ProductApiService::class.java)
    private val repository = ProductoRepository(api)

    @Test
    fun `getProductosSoloAPI devuelve lista desde la API`() = runBlocking {

        val dtoList = listOf(
            ProductoDto(
                id = 1,
                nombre = "Prod Test",
                descripcion = "Desc",
                precio = 1000.0,
                imagenUrl = "img",
                stockPorTalla = mapOf("M" to 5) // ← CORRECTO
            )
        )

        `when`(api.getProducts()).thenReturn(dtoList)

        val result = repository.getProductosSoloAPI()

        assertEquals(1, result.size)
        assertEquals("Prod Test", result[0].nombre)
    }
}
