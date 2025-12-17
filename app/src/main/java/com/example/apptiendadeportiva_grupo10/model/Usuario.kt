package com.example.apptiendadeportiva_grupo10.model

import com.google.gson.annotations.SerializedName


data class User(
    val id: Int? = null,
    val username: String,
    val password: String,
    val email: String,
    val rut: String?,
    val region: String,
    val comuna: String,
    val direccion: String,
    @SerializedName("esAdmin")
    val esAdmin: Boolean = false,
    val active: Boolean = true
)
