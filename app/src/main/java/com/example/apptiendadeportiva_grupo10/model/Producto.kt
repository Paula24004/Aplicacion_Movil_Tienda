package com.example.apptiendadeportiva_grupo10.model 

data class Producto (
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val precio: Int,
    val imagen: String?,
    val stockPorTalla: Map<String, Int>
)

fun ProductoEntity.toProducto(): Producto {
    return Producto(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        imagen = this.imagen,
        stockPorTalla = this.stockPorTalla
    )
}


