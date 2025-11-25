package com.example.apptiendadeportiva_grupo10.model

/**
 * Función de extensión para mapear un ProductoDto (objeto de red) a ProductoEntity (objeto de Room).
 * Se utiliza el operador Elvis (?:) para asignar valores por defecto a campos que podrían ser nulos
 * en el JSON de la API, asegurando la integridad de la base de datos local.
 */
fun ProductoDto.toEntity(): ProductoEntity {
    return ProductoEntity(
        id = this.id,
        // Proporciona un string vacío o un valor por defecto si el campo es null
        nombre = this.nombre ?: "Nombre Desconocido",
        descripcion = this.descripcion ?: "Sin descripción",
        precio = this.precio,
        // Usamos imagenUrl, que coincide con el DTO y la Entity
        imagenUrl = this.imagenUrl,
        stockPorTalla= this.stockPorTalla
    )
}

/**
 * Función de extensión para mapear un ProductoEntity (objeto de Room) a ProductoDto (objeto de red).
 * Útil para la capa de la UI si se necesita un objeto más ligero o si los datos se cargan desde la base de datos.
 */
fun ProductoEntity.toDto(): ProductoDto {
    return ProductoDto(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        imagenUrl = this.imagenUrl,
        stockPorTalla= this.stockPorTalla
    )
}