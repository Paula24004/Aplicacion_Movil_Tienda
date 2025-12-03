package com.example.apptiendadeportiva_grupo10.model

data class User(
    val id: Int? = null,
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val esAdmin: Boolean = false,
    val active: Boolean = true
)
