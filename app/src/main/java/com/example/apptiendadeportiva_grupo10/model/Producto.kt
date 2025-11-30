package com.example.apptiendadeportiva_grupo10.model

data class Producto (
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val precio: Double,
    val categoria: String?,
    val size: String?,
    val color: String?,
    val imagenUrl: String?,
    val stockPorTalla: Map<String, Int>?
)
fun ProductoEntity.toDomain(): Producto {
    val defaultPrice = 0.0

    return Producto(
        id = this.id,
        nombre = this.nombre ?: "Nombre Desconocido",
        descripcion = this.descripcion ?: "Sin descripción",
        precio = this.precio ?: defaultPrice,
        categoria = this.categoria ?: "Sin categoría",
        size = this.size ?: "Sin talla",
        color = this.color ?: "Sin color",
        imagenUrl = this.imagenUrl,
        stockPorTalla = this.stockPorTalla
    )
}


