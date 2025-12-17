package com.example.apptiendadeportiva_grupo10.model

import com.google.gson.annotations.SerializedName

data class ProductoDto(
    val id: Int? = null,
    @SerializedName("name")
    val nombre: String?,
    @SerializedName("description")
    val descripcion: String?,
    @SerializedName("price")
    val precio: Double?,
    @SerializedName("category")
    val categoria: String?,
    @SerializedName("size")
    val size: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("imagenUrl")
    val imagenUrl: String?,
    @SerializedName("stockPorTalla")
    val stockPorTalla: Map<String, Int>?
)



