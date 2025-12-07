package com.example.apptiendadeportiva_grupo10.repository

import com.example.apptiendadeportiva_grupo10.api.UserApi
import com.example.apptiendadeportiva_grupo10.data.remote.RetrofitClient
import com.example.apptiendadeportiva_grupo10.model.User

class UserRepository {

    private val userApi: UserApi = RetrofitClient.userApi

    suspend fun register(user: User) = userApi.register(user)

    // 🔥 LOGIN usando el nuevo modelo User del backend
    suspend fun login(username: String, password: String): Boolean {

        // El backend pide un User COMPLETO (aunque solo use username/password)
        val loginUser = User(
            id = null,
            username = username,
            password = password,
            email = "",         // El backend no lo usa para login
            region = "",
            comuna = "",
            direccion = "",
            esAdmin = false,
            active = true
        )

        val response = userApi.login(loginUser)
        return response.isSuccessful
    }
}
