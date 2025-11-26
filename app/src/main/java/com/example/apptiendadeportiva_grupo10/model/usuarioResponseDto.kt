package com.example.apptiendadeportiva_grupo10.model

import com.google.gson.annotations.SerializedName

data class usuarioResponseDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("username")
    val username: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("esAdmin")
    val esAdmin: Boolean?
)
