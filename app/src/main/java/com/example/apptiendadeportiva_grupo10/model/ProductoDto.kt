package com.example.apptiendadeportiva_grupo10.model

import com.google.gson.annotations.SerializedName

data class ProductoDto(
    val id: Int,
    @SerializedName("name")
    val nombre: String?,
    @SerializedName("description")
    val descripcion: String?,
    @SerializedName("price")
    val precio: Double?,
    @SerializedName("imageUrl")
    val imagenUrl: String?,
    @SerializedName("stock")
    val stock:Int?
)
