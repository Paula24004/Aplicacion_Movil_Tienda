package com.example.apptiendadeportiva_grupo10.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.apptiendadeportiva_grupo10.model.FakeDatabase
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.model.Usuario
import com.example.apptiendadeportiva_grupo10.repository.ProductoRepository
import com.example.apptiendadeportiva_grupo10.model.toDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val rut: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registrationSuccess: Boolean = false
)

// CAMBIO CLAVE: Usamos AndroidViewModel e inyectamos el ProductoRepository
class AuthViewModel(
    application: Application,
    private val productoRepository: ProductoRepository // Inyección del Repositorio
) : AndroidViewModel(application) {
    private val context = application.applicationContext

    var uiState by mutableStateOf(AuthUiState())
        private set

    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf("")
    // CAMBIO: listaProductos debe ser List<Producto> observable para coroutines
    var listaProductos = mutableStateOf<List<Producto>>(emptyList())
    val mensajeadmin = mutableStateOf("")

    private val _esAdminLogueado = MutableStateFlow<Boolean?>(null)
    val esAdminLogueado: StateFlow<Boolean?> = _esAdminLogueado

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
    fun registrar() {
        val rutValido = rutValido(uiState.rut)

        if (!rutValido) {
            uiState = uiState.copy(errorMessage = "El RUT ingresado no es válido (sin DV, 7 a 8 dígitos).")
            return
        }

        // Simular que el registro está en curso
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        val nuevo = Usuario(
            id = 0,
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
                errorMessage = null
            )
            mensaje.value = "Registro exitoso"
        } else {
            uiState = uiState.copy(
                registrationSuccess = false,
                isLoading = false,
                errorMessage = "El usuario/email ya existe en la base de datos."
            )
            mensaje.value = "El usuario ya existe"
        }
    }

    private fun rutValido(rut: String): Boolean {
        val limpio = rut.replace("[^0-9Kk]".toRegex(), "")
        val cuerpo = if (limpio.length > 1) limpio.dropLast(1) else return false
        return cuerpo.matches(Regex("[0-9]+")) && limpio.length in 7..8
    }
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
        _esAdminLogueado.value = null

        val loginExitoso = FakeDatabase.loginAdmin(usernameAdmin, passwordAdmin)

        if (loginExitoso) {
            mensajeadmin.value = "Login de administrador exitoso"
            _esAdminLogueado.value = true
            return true
        } else {
            mensajeadmin.value = "Usuario o contraseña de administrador incorrectos"
            _esAdminLogueado.value = false
            return false
        }
    }

    fun logoutAdmin() {
        usuarioActual.value = ""
        mensajeadmin.value = "Sesión de administrador cerrada"
        _esAdminLogueado.value = false
    }


    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                // 1. Llama al repositorio para hacer el POST al API y guardar en Room.
                productoRepository.insertProducto(context, producto)

                // 2. 🚀 CRÍTICO: Después de guardar, recarga la lista completa desde Room.
                // Esto garantiza que la lista 'listaProductos' tenga la versión con la ID correcta.
                cargarProductos()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun cargarProductos() {
        viewModelScope.launch {
            try {
                val entities = productoRepository.getProductos(context)
                val domainList = entities.map { it.toDomain() }
                listaProductos.value = domainList // Actualiza el State
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    fun buscarProducto(idProducto: Int): Producto? {
        return listaProductos.value.find { it.id == idProducto }
    }
    fun eliminarProducto(idProducto: Int) {
        viewModelScope.launch {
            try {
                // 1. Eliminar en Room (llamada suspendida)
                productoRepository.deleteProducto(context, idProducto)

                // 2. Actualizar la lista reactiva eliminando el producto
                listaProductos.value = listaProductos.value.filter { it.id != idProducto }
            } catch (e: Exception) {
                // Manejar errores de eliminación
                e.printStackTrace()
            }
        }
    }
}