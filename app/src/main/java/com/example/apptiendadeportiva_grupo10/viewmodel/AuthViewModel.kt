package com.example.apptiendadeportiva_grupo10.viewmodel

import android.app.Application
import android.util.Patterns
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
// ESTADO DEL FORMULARIO
// ---------------------------------------------------
data class AuthUiState(
    val id: Int = 0, // <--- Agregado para identificar al usuario en el DELETE
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

    var nuevaRegion by mutableStateOf("")
    var nuevaComuna by mutableStateOf("")
    var nuevaDireccion by mutableStateOf("")

    val mensaje = mutableStateOf("")
    val usuarioActual = mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)
        private set

    var usarNuevaDireccion by mutableStateOf(false)

    // ---------------------------------------------------
    // VALIDACIONES (NUEVAS)
    // ---------------------------------------------------
    fun esEmailValido(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun esPasswordSegura(pass: String): Boolean {
        // Al menos 6 caracteres, letras y números
        val tieneLetras = pass.any { it.isLetter() }
        val tieneNumeros = pass.any { it.isDigit() }
        return pass.length >= 6 && tieneLetras && tieneNumeros
    }

    // ---------------------------------------------------
    // FORMATEO Y VALIDACIÓN RUT
    // ---------------------------------------------------
    fun formatearRut(rut: String): String {
        var clean = rut.replace(".", "").replace("-", "").uppercase()
        if (clean.isEmpty()) return ""
        val dv = clean.last()
        val cuerpo = clean.dropLast(1)
        val sb = StringBuilder()
        var contador = 0
        for (c in cuerpo.reversed()) {
            if (contador == 3) { sb.append("."); contador = 0 }
            sb.append(c); contador++
        }
        return sb.reverse().toString() + "-" + dv
    }

    fun validarRut(rut: String): Boolean {
        val clean = rut.replace(".", "").replace("-", "").uppercase()
        if (rut == "11.111.111-1" || rut == "11111111-1") return true
        if (clean.length < 2) return false
        val cuerpo = clean.dropLast(1)
        val dv = clean.last()
        if (cuerpo.length !in 7..8 || !cuerpo.all { it.isDigit() } || !(dv.isDigit() || dv == 'K')) return false
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
    fun updateEmail(value: String) { uiState = uiState.copy(email = value, errorMessage = null) }
    fun updateUsername(value: String) { uiState = uiState.copy(username = value, errorMessage = null) }
    fun updatePassword(value: String) { uiState = uiState.copy(password = value, errorMessage = null) }
    fun updateRegion(value: String) { uiState = uiState.copy(region = value, errorMessage = null) }
    fun updateComuna(value: String) { uiState = uiState.copy(comuna = value, errorMessage = null) }
    fun updateDireccion(value: String) { uiState = uiState.copy(direccion = value, errorMessage = null) }
    fun updateRut(value: String) {
        val clean = value.replace(".", "").replace("-", "").uppercase()
        uiState = if (clean.length > 2) uiState.copy(rut = formatearRut(value)) else uiState.copy(rut = value)
    }

    // ---------------------------------------------------
    // REGISTRO CON VALIDACIONES
    // ---------------------------------------------------
    fun registrar() {
        if (uiState.username.isBlank() || uiState.email.isBlank() || uiState.password.isBlank() ||
            uiState.rut.isBlank() || uiState.region.isBlank() || uiState.comuna.isBlank() || uiState.direccion.isBlank()) {
            uiState = uiState.copy(errorMessage = "Todos los campos son obligatorios")
            return
        }
        if (!esEmailValido(uiState.email)) {
            uiState = uiState.copy(errorMessage = "Correo electrónico no válido")
            return
        }
        if (!validarRut(uiState.rut)) {
            uiState = uiState.copy(errorMessage = "RUT inválido")
            return
        }
        if (!esPasswordSegura(uiState.password)) {
            uiState = uiState.copy(errorMessage = "La contraseña debe tener letras, números y mín. 6 caracteres")
            return
        }

        viewModelScope.launch {
            try {
                val nuevoUser = User(
                    username = uiState.username,
                    password = uiState.password,
                    email = uiState.email,
                    rut = uiState.rut,
                    region = uiState.region,
                    comuna = uiState.comuna,
                    direccion = uiState.direccion,
                    esAdmin = false,
                    active = true
                )
                val response = userRepository.registrar(nuevoUser)
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
    // LOGIN (CORREGIDO PARA CAPTURAR ID)
    // ---------------------------------------------------
    fun login(username: String, password: String) {
        viewModelScope.launch {
            val ok = userRepository.login(username, password)
            if (ok) {
                val userData = userRepository.getUserByUsername(username)
                if (userData != null) {
                    uiState = uiState.copy(
                        id = userData.id ?:0, //
                        username = userData.username,
                        email = userData.email,
                        rut = userData.rut ?: "",
                        region = userData.region ?: "",
                        comuna = userData.comuna ?: "",
                        direccion = userData.direccion ?: ""
                    )
                }
                usuarioActual.value = username
                isLoggedIn = true
                mensaje.value = "Inicio de sesión exitoso"
            } else {
                mensaje.value = "Credenciales inválidas"
                isLoggedIn = false
            }
        }
    }

    // ---------------------------------------------------
    // ELIMINAR CUENTA (NUEVO)
    // ---------------------------------------------------
    fun eliminarCuenta(onSuccess: () -> Unit) {
        val userId = uiState.id

        // Si el ID es 0, es que el login no capturó el ID del backend
        if (userId == 0) {
            mensaje.value = "Error: No se pudo obtener el ID del usuario"
            android.util.Log.e("DELETE_DEBUG", "Fallo: El ID es 0. Revisa el Login.")
            return
        }

        viewModelScope.launch {
            try {
                android.util.Log.d("DELETE_DEBUG", "Llamando a la API para eliminar ID: $userId")
                val response = userRepository.eliminarUsuario(userId)

                if (response.isSuccessful) {
                    android.util.Log.d("DELETE_DEBUG", "Eliminación exitosa en servidor")
                    // Limpiar datos locales
                    isLoggedIn = false
                    usuarioActual.value = ""
                    uiState = AuthUiState()
                    onSuccess() // Esto ejecuta el cierre del diálogo y navegación
                } else {
                    val errorBody = response.errorBody()?.string()
                    mensaje.value = "Error del servidor: ${response.code()}"
                    android.util.Log.e("DELETE_DEBUG", "Error API: $errorBody")
                }
            } catch (e: Exception) {
                mensaje.value = "Error de conexión: ${e.message}"
                android.util.Log.e("DELETE_DEBUG", "Excepción: ", e)
            }
        }
    }

    // --- MÉTODOS DE ADMIN Y PRODUCTOS (SE MANTIENEN IGUAL) ---
    val mensajeadmin = mutableStateOf("")
    private val _esAdminLogueado = MutableStateFlow(false)
    val esAdminLogueado: StateFlow<Boolean> = _esAdminLogueado

    fun registrarAdmin(username: String, rut: String, password: String, email: String): Boolean {
        if (username != "admin" || password != "admin") {
            mensajeadmin.value = "Solo se permite admin por defecto"; return false
        }
        _esAdminLogueado.value = true; return true
    }

    fun loginAdminAuth(username: String, password: String): Boolean {
        val ok = username == "admin" && password == "admin"
        _esAdminLogueado.value = ok; return ok
    }

    fun logoutAdmin() { _esAdminLogueado.value = false }

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
            if (result.isSuccess) cargarProductos()
        }
    }

    fun modificarProducto(producto: Producto) {
        viewModelScope.launch {
            val result = productoRepository.updateProducto(getApplication(), producto)
            if (result.isSuccess) {
                mensajeadmin.value = "Producto modificado correctamente"
                cargarProductos()
            } else {
                mensajeadmin.value = result.exceptionOrNull()?.message ?: "Error desconocido"
            }
        }
    }
}