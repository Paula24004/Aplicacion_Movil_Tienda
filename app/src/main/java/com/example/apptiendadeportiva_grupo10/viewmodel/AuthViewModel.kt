package com.example.apptiendadeportiva_grupo10.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.apptiendadeportiva_grupo10.model.FakeDatabase
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow // Importación necesaria
import kotlinx.coroutines.flow.StateFlow      // Importación necesaria

data class AuthUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val rut: String = "", // Nuevo campo para el RUT
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registrationSuccess: Boolean = false
)

class AuthViewModel : ViewModel() {

    // Estado observable para la UI
    var uiState by mutableStateOf(AuthUiState())
        private set

    // Variables internas que no dependen del AuthUiState
    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf("")
    var listaProductos = mutableStateListOf<Producto>()
    val mensajeadmin = mutableStateOf("")

    // --- ESTADO AÑADIDO PARA LA NAVEGACIÓN DEL ADMINISTRADOR ---
    private val _esAdminLogueado = MutableStateFlow<Boolean?>(null)
    val esAdminLogueado: StateFlow<Boolean?> = _esAdminLogueado
    // -----------------------------------------------------------


    // ------------------------------------
    // Actualizadores de Estado para la UI
    // ------------------------------------

    fun updateUsername(newUsername: String) {
        uiState = uiState.copy(username = newUsername, errorMessage = null)
    }

    fun updateEmail(newEmail: String) {
        uiState = uiState.copy(email = newEmail, errorMessage = null)
    }

    fun updatePassword(newPassword: String) {
        uiState = uiState.copy(password = newPassword, errorMessage = null)
    }

    fun updateRut(newRut: String) {
        uiState = uiState.copy(rut = newRut, errorMessage = null)
    }


    // ------------------------------------
    // LÓGICA DE REGISTRO
    // ------------------------------------

    fun registrar() {
        val rutValido = rutValido(uiState.rut)

        if (!rutValido) {
            uiState = uiState.copy(errorMessage = "El RUT ingresado no es válido (sin DV, 7 a 8 dígitos).")
            return
        }

        // Simular que el registro está en curso
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        val nuevo = Usuario(
            id = 0, // ID 0 por ahora, ya que la FakeDatabase no lo usa
            nombre = uiState.username,
            rut = uiState.rut,
            password = uiState.password,
            email = uiState.email
        )

        val registroExitoso = FakeDatabase.registrar(nuevo)

        // Simulación de respuesta de la FakeDatabase
        if (registroExitoso) {
            uiState = uiState.copy(
                registrationSuccess = true,
                isLoading = false,
                errorMessage = null // Limpiar mensaje de error si fue exitoso
            )
            mensaje.value = "Registro exitoso" // Para compatibilidad con otras partes
        } else {
            uiState = uiState.copy(
                registrationSuccess = false,
                isLoading = false,
                errorMessage = "El usuario/email ya existe en la base de datos."
            )
            mensaje.value = "El usuario ya existe" // Para compatibilidad con otras partes
        }
    }

    // Función de validación del RUT (copiada de su código anterior)
    private fun rutValido(rut: String): Boolean {
        val limpio = rut.replace("[^0-9Kk]".toRegex(), "")
        val cuerpo = if (limpio.length > 1) limpio.dropLast(1) else return false
        return cuerpo.matches(Regex("[0-9]+")) && limpio.length in 7..8
    }

    // ------------------------------------
    // LÓGICA DE USUARIO Y ADMIN (Manteniendo la estructura anterior)
    // ------------------------------------

    fun login(email: String, password: String): Boolean {
        return if (FakeDatabase.login(email, password)) {
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

    fun registrarAdmin(usernameAdmin: String, rutAdmin: String, passwordAdmin: String, emailAdmin: String): Boolean {
        if (FakeDatabase.registrarAdmin(usernameAdmin, rutAdmin, passwordAdmin, emailAdmin)) {
            mensajeadmin.value = "Registro de administrador exitoso"
            return true
        }
        mensajeadmin.value = "El administrador ya existe"
        return false
    }

    fun loginAdminAuth(usernameAdmin: String, passwordAdmin: String): Boolean {
        // Al intentar login, reiniciamos el estado observable
        _esAdminLogueado.value = null

        val loginExitoso = FakeDatabase.loginAdmin(usernameAdmin, passwordAdmin)

        if (loginExitoso) {
            mensajeadmin.value = "Login de administrador exitoso"
            _esAdminLogueado.value = true // AVISAMOS A COMPOSE: LOGIN EXITOSO
            return true
        } else {
            mensajeadmin.value = "Usuario o contraseña de administrador incorrectos"
            _esAdminLogueado.value = false // AVISAMOS A COMPOSE: LOGIN FALLIDO
            return false
        }
    }

    fun logoutAdmin() {
        usuarioActual.value = ""
        mensajeadmin.value = "Sesión de administrador cerrada"
        _esAdminLogueado.value = false // AVISAMOS A COMPOSE: LOGOUT
    }


    // ------------------------------------
    // LÓGICA DE PRODUCTOS
    // ------------------------------------

    fun agregarProducto(producto: Producto) {
        listaProductos.add(producto)
    }

    fun buscarProducto(idProducto: Int): Producto? {
        return listaProductos.find { it.id == idProducto }
    }

    fun eliminarProducto(idProducto: Int): Boolean {
        val productoAEliminar = listaProductos.find { it.id == idProducto }
        return if (productoAEliminar != null) {
            listaProductos.remove(productoAEliminar)
        } else {
            false
        }
    }
}