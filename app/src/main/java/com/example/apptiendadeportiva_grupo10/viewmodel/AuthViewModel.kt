package com.example.apptiendadeportiva_grupo10.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.model.toDomain
import com.example.apptiendadeportiva_grupo10.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// ===================================================
// ESTADO PARA USUARIOS NORMALES
// ===================================================
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
    private val productoRepository: ProductoRepository
) : AndroidViewModel(application) {

    // ---------------------------------------------------
    // LOGIN Y REGISTRO DE USUARIOS NORMALES
    // ---------------------------------------------------
    var uiState by mutableStateOf(AuthUiState())
        private set

    val mensaje = mutableStateOf("")
    val usuarioActual = mutableStateOf("")

    fun updateEmail(value: String) { uiState = uiState.copy(email = value, errorMessage = null) }
    fun updateUsername(value: String) { uiState = uiState.copy(username = value, errorMessage = null) }
    fun updatePassword(value: String) { uiState = uiState.copy(password = value, errorMessage = null) }
    fun updateRut(value: String) { uiState = uiState.copy(rut = value, errorMessage = null) }

    fun registrar() {
        if (uiState.username.isBlank() ||
            uiState.email.isBlank() ||
            uiState.password.isBlank() ||
            uiState.rut.isBlank()
        ) {
            uiState = uiState.copy(errorMessage = "Todos los campos son obligatorios")
            return
        }

        uiState = uiState.copy(
            registrationSuccess = true,
            errorMessage = null
        )
        mensaje.value = "Registro exitoso"
    }

    fun login(email: String, password: String): Boolean {
        return if (email == "user@gmail.com" && password == "1234") {
            usuarioActual.value = email
            mensaje.value = "Inicio de sesión exitoso"
            true
        } else {
            mensaje.value = "Credenciales inválidas"
            false
        }
    }

    fun logout() {
        usuarioActual.value = ""
        mensaje.value = "Sesión cerrada"
    }

    // ---------------------------------------------------
    // LOGIN Y REGISTRO ADMINISTRADOR
    // ---------------------------------------------------
    val mensajeadmin = mutableStateOf("")
    private val _esAdminLogueado = MutableStateFlow(false)
    val esAdminLogueado: StateFlow<Boolean> = _esAdminLogueado

    /**
     * REGISTRO DE ADMIN
     * Ya no loguea automáticamente
     */
    fun registrarAdmin(username: String, rut: String, password: String, email: String): Boolean {
        if (username.isEmpty() || rut.isEmpty() || password.isEmpty() || email.isEmpty()) {
            mensajeadmin.value = "Todos los campos son obligatorios"
            return false
        }

        // Puedes cambiar esta validación si quieres admin libre
        if (username != "admin" || password != "admin") {
            mensajeadmin.value = "Solo se permite crear el usuario administrador por defecto"
            return false
        }

        // Registro exitoso
        _esAdminLogueado.value = true
        mensajeadmin.value = "Registro exitoso"
        return true
    }


    fun loginAdminAuth(username: String, password: String): Boolean {
        val ok = username == "admin" && password == "admin"
        _esAdminLogueado.value = ok
        mensajeadmin.value = if (ok) "Login de administrador exitoso" else "Usuario o contraseña incorrectos"
        return ok
    }

    fun logoutAdmin() {
        _esAdminLogueado.value = false
        mensajeadmin.value = "Sesión de administrador cerrada"
    }

    // ---------------------------------------------------
    // PRODUCTOS
    // ---------------------------------------------------
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
                cargarProductos() // Recarga la lista
            } else {
                mensajeadmin.value = result.exceptionOrNull()?.message ?: "Error desconocido al modificar"
            }
        }
    }
}
