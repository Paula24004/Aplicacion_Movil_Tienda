package com.example.apptiendadeportiva_grupo10.pruebasunitarias

import org.junit.Assert.assertEquals
import org.junit.Test

class CompraExitosaLogicTest {

    @Test
    fun `calculo de subtotal iva y total es correcto`() {

        // GIVEN
        val totalRecibido = 10000.0

        // WHEN
        val subtotal = totalRecibido
        val iva = subtotal * 0.19
        val total = subtotal + iva

        // THEN
        assertEquals(10000.0, subtotal, 0.01)
        assertEquals(1900.0, iva, 0.01)
        assertEquals(11900.0, total, 0.01)
    }
}
