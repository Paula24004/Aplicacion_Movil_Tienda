package com.example.apptiendadeportiva_grupo10.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64


fun ProductoDto.toEntity(): ProductoEntity {
    return ProductoEntity(
        id = this.id ?: 0,
        nombre = this.nombre ?: "Nombre Desconocido",
        descripcion = this.descripcion ?: "Sin descripción",
        precio = this.precio,
        categoria = this.categoria ?: "Sin categoría",
        size = this.size ?: "Sin talla",
        color = this.color ?: "Sin color",
        imagenUrl = this.imagenUrl,
        stockPorTalla = this.stockPorTalla
    )
}

fun ProductoEntity.toDto(): ProductoDto {
    return ProductoDto(
        id = if (this.id == 0) null else this.id, // API genera ID
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        categoria = this.categoria,
        size = this.size,
        color = this.color,
        imagenUrl = this.imagenUrl,
        stockPorTalla = this.stockPorTalla
    )
}

fun Producto.toDto(): ProductoDto {
    return ProductoDto(
        id = if (this.id == 0) null else this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        categoria = this.categoria,
        size = this.size,
        color = this.color,
        imagenUrl = this.imagenUrl,
        stockPorTalla = this.stockPorTalla
    )
}


// Función de extensión para convertir el string de la DB en algo que Android entienda
fun Producto.getBitmap(): Bitmap? {
    if (this.imagenUrl == null || !this.imagenUrl.startsWith("data:image")) return null

    return try {
        val base64Data = this.imagenUrl.substringAfter(",")
        val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }
}
