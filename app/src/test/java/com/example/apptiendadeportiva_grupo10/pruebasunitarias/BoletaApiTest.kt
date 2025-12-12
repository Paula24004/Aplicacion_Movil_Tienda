package com.example.apptiendadeportiva_grupo10.pruebasunitarias

import com.example.apptiendadeportiva_grupo10.api.BoletaApi
import com.example.apptiendadeportiva_grupo10.model.BoletaDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class BoletaApiTest {

    private val fakeApi = object : BoletaApi {
        override suspend fun crearBoleta(boleta: BoletaDto): BoletaDto {
            return boleta
        }
    }

    @Test
    fun `crearBoleta retorna la boleta enviada`() = runBlocking {

        val boleta = BoletaDto(
            cantidad = 2,
            precio = 15000,
            idProduct = 10
        )

        val result = fakeApi.crearBoleta(boleta)

        assertEquals(2, result.cantidad)
        assertEquals(15000, result.precio)
        assertEquals(10, result.idProduct)
    }
}
