package com.example.apptiendadeportiva_grupo10.model
fun ProductoDto.toEntity(): ProductoEntity {
    return ProductoEntity(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        imagen = this.imagen,
        stockPorTalla = this.stockPorTalla
    )
}

fun ProductoEntity.toDto(): ProductoDto {
    return ProductoDto(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        imagen = this.imagen,
        stockPorTalla = this.stockPorTalla
    )
}