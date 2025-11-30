package com.example.apptiendadeportiva_grupo10.model

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
