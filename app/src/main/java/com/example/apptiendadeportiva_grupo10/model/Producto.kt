package com.example.apptiendadeportiva_grupo10.model 

data class Producto (
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val precio: Int,
    val imagen: String?
)