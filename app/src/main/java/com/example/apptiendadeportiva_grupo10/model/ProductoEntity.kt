package com.example.apptiendadeportiva_grupo10.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey val id: Int,
    val nombre: String?,
    val descripcion: String?,
    val precio: Double?,
    val imagenUrl: String?,
    val stockPorTalla: Map<String, Int>?
)
