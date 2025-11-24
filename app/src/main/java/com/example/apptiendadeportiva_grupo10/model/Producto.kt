package com.example.apptiendadeportiva_grupo10.model

data class Producto (
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val precio: Double,
    val imagenUrl: String?,

    val stock: Int?
)
fun ProductoEntity.toDomain(): Producto {
    val defaultPrice = 0.0

    return Producto(
        id = this.id,
        nombre = this.nombre ?: "Nombre Desconocido",
        descripcion = this.descripcion ?: "Sin descripción",
        precio = this.precio ?: defaultPrice,
        imagenUrl = this.imagenUrl,
        stock= this.stock
    )
}


