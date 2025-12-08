package com.example.apptiendadeportiva_grupo10.repository

import com.example.apptiendadeportiva_grupo10.api.UserApi
import com.example.apptiendadeportiva_grupo10.data.remote.RetrofitClient
import com.example.apptiendadeportiva_grupo10.model.User
import com.example.apptiendadeportiva_grupo10.model.LoginRequest

class UserRepository {

    private val userApi: UserApi = RetrofitClient.userApi

    // 🔹 REGISTRO
    suspend fun registrar(user: User) = userApi.register(user)

    // 🔹 LOGIN CORRECTO (usando LoginRequest)
    suspend fun login(username: String, password: String): Boolean {
        val request = LoginRequest(username, password)
        val response = userApi.login(request)

        return if (response.isSuccessful) {
            val body = response.body()?.string() ?: ""
            body.contains("exitos", ignoreCase = true)
        } else {
            false
        }
    }

    suspend fun getUserByUsername(username: String): User? {
        return try {
            userApi.getUserByUsername(username)
        } catch (e: Exception) {
            null
        }
    }


}
