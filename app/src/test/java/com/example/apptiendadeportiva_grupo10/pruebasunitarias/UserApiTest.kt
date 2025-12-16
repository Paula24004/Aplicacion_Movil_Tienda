package com.example.apptiendadeportiva_grupo10.pruebasunitarias

import com.example.apptiendadeportiva_grupo10.api.UserApi
import com.example.apptiendadeportiva_grupo10.model.LoginRequest
import com.example.apptiendadeportiva_grupo10.model.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.Response
import org.junit.Assert.*
import org.junit.Test
import kotlinx.coroutines.runBlocking

class UserApiTest {

    private val fakeApi = object : UserApi {

        override suspend fun getUserByUsername(username: String): User {
            return User(
                username = username,
                password = "1234",
                email = "test@test.cl",
                rut = "11.111.111-1",
                region = "RM",
                comuna = "Santiago",
                direccion = "Calle Falsa 123",
                esAdmin = false,
                active = true
            )
        }

        override suspend fun register(user: User): Response<User> {
            return Response.success(user)
        }

        override suspend fun login(request: LoginRequest): Response<ResponseBody> {
            val body = ResponseBody.create(
                "text/plain".toMediaTypeOrNull(),
                "OK"
            )
            return Response.success(body)
        }

        override suspend fun eliminarUsuario(id: Int): Response<Void> {
            return Response.success(null)
        }
    }

    @Test
    fun `getUserByUsername retorna usuario correcto`() = runBlocking {

        val user = fakeApi.getUserByUsername("paula")

        assertEquals("paula", user.username)
        assertEquals("test@test.cl", user.email)
        assertEquals("RM", user.region)
        assertTrue(user.active)
    }

    @Test
    fun `login retorna respuesta exitosa`() = runBlocking {

        val response = fakeApi.login(LoginRequest("paula", "1234"))

        assertTrue(response.isSuccessful)
        assertNotNull(response.body())
    }

    @Test
    fun `register retorna usuario registrado`() = runBlocking {

        val user = User(
            username = "nuevo",
            password = "1234",
            email = "nuevo@test.cl",
            rut = "11.111.111-1",
            region = "RM",
            comuna = "Santiago",
            direccion = "Direccion 123",
            esAdmin = false,
            active = true
        )

        val response = fakeApi.register(user)

        assertTrue(response.isSuccessful)
        assertEquals("nuevo", response.body()?.username)
    }

    @Test
    fun `eliminarUsuario retorna respuesta exitosa`() = runBlocking {

        val response = fakeApi.eliminarUsuario(1)

        assertTrue(response.isSuccessful)
    }
}
