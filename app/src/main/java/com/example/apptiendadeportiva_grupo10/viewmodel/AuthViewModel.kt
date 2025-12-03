package com.example.apptiendadeportiva_grupo10.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.model.User
import com.example.apptiendadeportiva_grupo10.model.toDomain
import com.example.apptiendadeportiva_grupo10.repository.ProductoRepository
import com.example.apptiendadeportiva_grupo10.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// ESTADO PARA USUARIOS NORMALES
data class AuthUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val rut: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registrationSuccess: Boolean = false
)

class AuthViewModel(
    application: Application,
    private val productoRepository: ProductoRepository,
) : AndroidViewModel(application) {
    // REPOSITORIO DE USUARIOS (BACKEND)
    private val userRepository = UserRepository()
    // LOGIN Y REGISTRO DE USUARIOS NORMALES
    var uiState by mutableStateOf(AuthUiState())
        private set

    val mensaje = mutableStateOf("")
    val usuarioActual = mutableStateOf("")

    fun updateEmail(value: String) { uiState = uiState.copy(email = value, errorMessage = null) }
    fun updateUsername(value: String) { uiState = uiState.copy(username = value, errorMessage = null) }
    fun updatePassword(value: String) { uiState = uiState.copy(password = value, errorMessage = null) }
    fun updateRut(value: String) { uiState = uiState.copy(rut = value, errorMessage = null) }

    // REGISTRO REAL VIA API
    fun registrar() {
        if (uiState.username.isBlank() ||
            uiState.email.isBlank() ||
            uiState.password.isBlank() ||
            uiState.rut.isBlank()
        ) {
            uiState = uiState.copy(errorMessage = "Todos los campos son obligatorios")
            return
        }

        viewModelScope.launch {
            try {
                val nuevoUser = User(
                    username = uiState.username,
                    email = uiState.email,
                    password = uiState.password,
                    esAdmin = false,
                    active = true
                )

                val response = userRepository.register(nuevoUser)

                if (response.isSuccessful) {
                    uiState = uiState.copy(registrationSuccess = true, errorMessage = null)
                    mensaje.value = "Registro exitoso"
                } else {
                    mensaje.value = "El email o username ya existe"
                }

            } catch (e: Exception) {
                mensaje.value = "Error al registrar usuario"
            }
        }
    }

    //LOGIN REAL VIA API
    fun login(email: String, password: String): Boolean {
        viewModelScope.launch {
            val ok = userRepository.login(email, password)
            if (ok) {
                usuarioActual.value = email
                mensaje.value = "Inicio de sesión exitoso"
            } else {
                mensaje.value = "Credenciales inválidas"
            }
        }
        return false
    }

    fun logout() {
        usuarioActual.value = ""
        mensaje.value = "Sesión cerrada"
    }

    // LOGIN Y REGISTRO ADMINISTRADOR (SE MANTIENE IGUAL)
    val mensajeadmin = mutableStateOf("")
    private val _esAdminLogueado = MutableStateFlow(false)
    val esAdminLogueado: StateFlow<Boolean> = _esAdminLogueado

    fun registrarAdmin(username: String, rut: String, password: String, email: String): Boolean {
        if (username.isEmpty() || rut.isEmpty() || password.isEmpty() || email.isEmpty()) {
            mensajeadmin.value = "Todos los campos son obligatorios"
            return false
        }

        if (username != "admin" || password != "admin") {
            mensajeadmin.value = "Solo se permite crear admin por defecto"
            return false
        }

        _esAdminLogueado.value = true
        mensajeadmin.value = "Registro exitoso"
        return true
    }

    fun loginAdminAuth(username: String, password: String): Boolean {
        val ok = username == "admin" && password == "admin"
        _esAdminLogueado.value = ok
        mensajeadmin.value = if (ok) "Login admin exitoso" else "Usuario o contraseña incorrectos"
        return ok
    }

    fun logoutAdmin() {
        _esAdminLogueado.value = false
        mensajeadmin.value = "Sesión de administrador cerrada"
    }

    // PRODUCTOS
    var listaProductos = mutableStateOf<List<Producto>>(emptyList())

    fun cargarProductos() {
        viewModelScope.launch {
            productoRepository.getProductos(getApplication()).collectLatest { productos ->
                listaProductos.value = productos.map { it.toDomain() }
            }
        }
    }

    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            val result = productoRepository.insertProducto(getApplication(), producto)
            if (result.isSuccess) {
                mensajeadmin.value = "Producto agregado correctamente"
                cargarProductos()
            } else {
                mensajeadmin.value = result.exceptionOrNull()?.message ?: "Error desconocido"
            }
        }
    }

    fun eliminarProducto(id: Int) {
        viewModelScope.launch {
            val result = productoRepository.deleteProducto(getApplication(), id)
            if (result.isSuccess) {
                mensajeadmin.value = "Producto eliminado"
                cargarProductos()
            } else {
                mensajeadmin.value = result.exceptionOrNull()?.message ?: "Error desconocido"
            }
        }
    }

    fun modificarProducto(producto: Producto) {
        viewModelScope.launch {
            val result = productoRepository.updateProducto(getApplication(), producto)
            if (result.isSuccess) {
                mensajeadmin.value = "Producto modificado correctamente"
                cargarProductos()
            } else {
                mensajeadmin.value =
                    result.exceptionOrNull()?.message ?: "Error desconocido al modificar"
            }
        }
    }
}
