package com.example.apptiendadeportiva_grupo10.model

import com.google.gson.annotations.SerializedName

data class usuarioRegisterDto(
    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
