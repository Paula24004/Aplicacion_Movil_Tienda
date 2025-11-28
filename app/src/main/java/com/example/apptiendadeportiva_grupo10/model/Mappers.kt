package com.example.apptiendadeportiva_grupo10.model
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

fun Producto.toEntity(): ProductoEntity {
    // Usamos el operador Elvis (?:) ya que el precio en Producto es no nulo (Double),
    // pero la entidad de Room (ProductoEntity) lo acepta como nullable (Double?)
    return ProductoEntity(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        imagenUrl = this.imagenUrl,
        stockPorTalla = this.stockPorTalla
    )
}

fun Producto.toDto(): ProductoDto {
    return ProductoDto(
        // Al enviar un nuevo producto, el ID puede ser el que tiene actualmente (aunque el servidor
        // debe ignorarlo y asignar uno nuevo) o puedes pasarlo como 0/null si el DTO lo permite.
        id = this.id,
        // Los campos de dominio (Producto) son en su mayoría no nulos, por lo que se pasan directamente.
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        imagenUrl = this.imagenUrl,
        stockPorTalla = this.stockPorTalla
    )
    }