package com.example.apptiendadeportiva_grupo10.pruebasunitarias

import com.example.apptiendadeportiva_grupo10.model.GestionEnvioDto
import org.junit.Assert.assertEquals
import org.junit.Test

class GestionEnvioLogicTest {

    @Test
    fun `se crea correctamente un GestionEnvioDto`() {

        // GIVEN
        val agencia = "Correos de Chile"
        val fecha = "12-12-2025"
        val estado = "En despacho"
        val direccion = "Av Siempre Viva 123"

        // WHEN
        val envio = GestionEnvioDto(
            agenciaEnvio = agencia,
            fechaEnvio = fecha,
            estadoEnvio = estado,
            direccionDespacho = direccion
        )

        // THEN
        assertEquals("Correos de Chile", envio.agenciaEnvio)
        assertEquals("12-12-2025", envio.fechaEnvio)
        assertEquals("En despacho", envio.estadoEnvio)
        assertEquals("Av Siempre Viva 123", envio.direccionDespacho)
    }
}
