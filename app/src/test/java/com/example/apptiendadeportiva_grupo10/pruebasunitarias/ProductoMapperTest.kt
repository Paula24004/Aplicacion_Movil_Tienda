package com.example.apptiendadeportiva_grupo10.pruebasunitarias

import com.example.apptiendadeportiva_grupo10.model.ProductoDto
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity
import com.example.apptiendadeportiva_grupo10.model.toDomain
import com.example.apptiendadeportiva_grupo10.model.toEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductoMapperTest {

    @Test
    fun `toEntity convierte correctamente un ProductoDto`() {
        val dto = ProductoDto(
            id = 1,
            nombre = "Zapatilla Test",
            descripcion = "Descripción Test",
            precio = 9990.0,
            imagenUrl = "https://ejemplo.com/img.png",
            stock = 10
        )

        val entity = dto.toEntity()

        assertEquals(1, entity.id)
        assertEquals("Zapatilla Test", entity.nombre)
        assertEquals("Descripción Test", entity.descripcion)
        assertEquals(9990.0, entity.precio ?: 0.0, 0.01)   // ← CORREGIDO
        assertEquals("https://ejemplo.com/img.png", entity.imagenUrl)
        assertEquals(10, entity.stock)
    }

    @Test
    fun `toDomain convierte correctamente un ProductoEntity`() {
        val entity = ProductoEntity(
            id = 1,
            nombre = "Zapatilla Test",
            descripcion = "Desc Test",
            precio = 5000.0,
            imagenUrl = "url.png",
            stock = 5
        )

        val domain = entity.toDomain()

        assertEquals(1, domain.id)
        assertEquals("Zapatilla Test", domain.nombre)
        assertEquals("Desc Test", domain.descripcion)
        assertEquals(5000.0, domain.precio, 0.01)   // ← CORRECTO AQUÍ
        assertEquals("url.png", domain.imagenUrl)
        assertEquals(5, domain.stock)
    }
}
