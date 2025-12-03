package com.example.apptiendadeportiva_grupo10.pruebasunitarias

import com.example.apptiendadeportiva_grupo10.api.ProductApiService
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.MockResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import kotlinx.coroutines.runBlocking

class ApiServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var api: ProductApiService

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getProducts parsea correctamente el JSON`() = runBlocking {

        val json = """
            [
                {
                    "id": 1,
                    "name": "Zapatilla",
                    "description": "desc",
                    "price": 1000,
                    "category": "Calzado",
                    "size": "M",
                    "color": "Azul",
                    "imageUrl": "img.png",
                    "stockPorTalla": { "M": 5 }
                }
            ]
        """.trimIndent()

        server.enqueue(MockResponse().setBody(json).setResponseCode(200))

        val result = api.getProducts()

        assertEquals(1, result.size)
        assertEquals("Zapatilla", result[0].nombre)
        assertEquals("desc", result[0].descripcion)
        assertEquals(1000.0, result[0].precio!!, 0.01)
        assertEquals("Calzado", result[0].categoria)
        assertEquals("M", result[0].size)
        assertEquals("Azul", result[0].color)
        assertEquals("img.png", result[0].imagenUrl)
        assertEquals(5, result[0].stockPorTalla?.get("M"))
    }
}
