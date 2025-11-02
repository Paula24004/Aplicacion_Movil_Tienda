package com.example.apptiendadeportiva_grupo10.model



data class Usuario(
    val id: Int,
    val nombre: String?,
    val rut: String?,
    val password: String?,
    val email: String?,
    val esAdmin: Boolean = false,
    val usernameAdmin: String? = null
)
