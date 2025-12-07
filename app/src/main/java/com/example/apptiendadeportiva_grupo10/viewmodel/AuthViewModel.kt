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

// ---------------------------------------------------
// ESTADO FORM UI
// ---------------------------------------------------
data class AuthUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val rut: String = "",
    val region: String = "",
    val comuna: String = "",
    val direccion: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registrationSuccess: Boolean = false
)

// ---------------------------------------------------
// VIEWMODEL
// ---------------------------------------------------
class AuthViewModel(
    application: Application,
    private val productoRepository: ProductoRepository,
) : AndroidViewModel(application) {

    private val userRepository = UserRepository()

    var uiState by mutableStateOf(AuthUiState())
        private set

    val mensaje = mutableStateOf("")
    val usuarioActual = mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)
        private set

    // ---------------------------------------------------
    // FORMATEO DEL RUT
    // ---------------------------------------------------
    fun formatearRut(rut: String): String {
        var clean = rut.replace(".", "").replace("-", "").uppercase()
        if (clean.isEmpty()) return ""

        val dv = clean.last()
        val cuerpo = clean.dropLast(1)

        val sb = StringBuilder()
        var contador = 0

        for (c in cuerpo.reversed()) {
            if (contador == 3) {
                sb.append(".")
                contador = 0
            }
            sb.append(c)
            contador++
        }

        return sb.reverse().toString() + "-" + dv
    }

    // ---------------------------------------------------
    // VALIDACIÓN DEL RUT (8–9 dígitos + DV 0–9 o K + RUT de prueba)
    // ---------------------------------------------------
    fun validarRut(rut: String): Boolean {
        val clean = rut.replace(".", "").replace("-", "").uppercase()

        // ⭐ Permitir siempre el RUT de prueba
        if (rut == "11.111.111-1" || rut == "11111111-1") return true

        if (clean.length < 2) return false

        val cuerpo = clean.dropLast(1)
        val dv = clean.last()

        // ⭐ Cuerpo con 8–9 dígitos (7–8 + DV = 8–9 caracteres)
        if (cuerpo.length !in 7..8) return false

        // ⭐ Cuerpo solo números
        if (!cuerpo.all { it.isDigit() }) return false

        // ⭐ DV válido
        if (!(dv.isDigit() || dv == 'K')) return false

        // ⭐ Cálculo módulo 11
        var suma = 0
        var multiplicador = 2

        for (char in cuerpo.reversed()) {
            suma += char.digitToInt() * multiplicador
            multiplicador = if (multiplicador == 7) 2 else multiplicador + 1
        }

        val resultado = 11 - (suma % 11)
        val dvEsperado = when (resultado) {
            11 -> '0'
            10 -> 'K'
            else -> resultado.digitToChar()
        }

        return dv == dvEsperado
    }

    // ---------------------------------------------------
    // UPDATE CAMPOS
    // ---------------------------------------------------
    fun updateEmail(value: String) {
        uiState = uiState.copy(email = value, errorMessage = null)
    }

    fun updateUsername(value: String) {
        uiState = uiState.copy(username = value, errorMessage = null)
    }

    fun updatePassword(value: String) {
        uiState = uiState.copy(password = value, errorMessage = null)
    }

    fun updateRut(value: String) {
        val clean = value.replace(".", "").replace("-", "").uppercase()

        if (clean.length <= 2) {
            uiState = uiState.copy(rut = value)
            return
        }

        uiState = uiState.copy(
            rut = formatearRut(value),
            errorMessage = null
        )
    }

    fun updateRegion(value: String) {
        uiState = uiState.copy(region = value, errorMessage = null)
    }

    fun updateComuna(value: String) {
        uiState = uiState.copy(comuna = value, errorMessage = null)
    }

    fun updateDireccion(value: String) {
        uiState = uiState.copy(direccion = value, errorMessage = null)
    }

    // ---------------------------------------------------
    // REGISTRO
    // ---------------------------------------------------
    fun registrar() {
        if (
            uiState.username.isBlank() ||
            uiState.email.isBlank() ||
            uiState.password.isBlank() ||
            uiState.rut.isBlank() ||
            uiState.region.isBlank() ||
            uiState.comuna.isBlank() ||
            uiState.direccion.isBlank()
        ) {
            uiState = uiState.copy(errorMessage = "Todos los campos son obligatorios")
            return
        }

        if (!validarRut(uiState.rut)) {
            uiState = uiState.copy(errorMessage = "RUT inválido. Ejemplo: 12.345.678-5")
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

    // ---------------------------------------------------
    // LOGIN
    // ---------------------------------------------------
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val ok = userRepository.login(email, password)

            if (ok) {
                usuarioActual.value = email
                isLoggedIn = true
                mensaje.value = "Inicio de sesión exitoso"
            } else {
                mensaje.value = "Credenciales inválidas"
                isLoggedIn = false
            }
        }
    }

    fun logout() {
        usuarioActual.value = ""
        isLoggedIn = false
        mensaje.value = "Sesión cerrada"
    }

    // ---------------------------------------------------
    // ADMIN
    // ---------------------------------------------------
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
                mensajeadmin.value =
                    result.exceptionOrNull()?.message ?: "Error desconocido"
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
                mensajeadmin.value =
                    result.exceptionOrNull()?.message ?: "Error desconocido"
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
