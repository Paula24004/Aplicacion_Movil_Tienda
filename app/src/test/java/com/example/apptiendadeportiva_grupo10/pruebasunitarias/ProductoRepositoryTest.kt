package com.example.apptiendadeportiva_grupo10.pruebasunitarias

import android.content.Context
import com.example.apptiendadeportiva_grupo10.data.remote.ApiService
import com.example.apptiendadeportiva_grupo10.model.ProductoDto
import com.example.apptiendadeportiva_grupo10.repository.ProductoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ProductoRepositoryTest {

    private val context = mock(Context::class.java)
    private val api = mock(ApiService::class.java)
    private val repository = ProductoRepository(api)

    @Test
    fun `getProductos devuelve lista desde API cuando es exitoso`() = runBlocking {

        val dtoList = listOf(
            ProductoDto(1, "Prod", "desc", 1000.0, "img", 10)
        )

        `when`(api.getProducts()).thenReturn(dtoList)

        val result = repository.getProductos(context)

        assertEquals(1, result.size)
        assertEquals("Prod", result[0].nombre)
    }
}
