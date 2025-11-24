package com.example.apptiendadeportiva_grupo10.model

data class ProductoDto(
    val id: Int,
    val nombre: String?,
    val descripcion: String?,
    val precio: Double?,
    val imagenUrl: String?,
    val stockPorTalla: Map<String, Int>?
)
