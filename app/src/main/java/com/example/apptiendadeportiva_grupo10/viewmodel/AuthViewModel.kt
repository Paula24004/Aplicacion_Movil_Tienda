package com.example.apptiendadeportiva_grupo10.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.apptiendadeportiva_grupo10.model.FakeDatabase
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.model.Usuario

class AuthViewModel : ViewModel() {


    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf("")
    var listaProductos = mutableStateListOf<Producto>()

    // ---- Estado para ADMIN ----
    val mensajeadmin = mutableStateOf("")

    // ---------- USUARIO ----------
    fun registrar(id: Int, nombre: String?, password: String?, email: String?) {
        val nuevo = Usuario(id, nombre, password, email)
        if (FakeDatabase.registrar(nuevo)) {
            mensaje.value = "Registro exitoso"
        } else {
            mensaje.value = "El usuario ya existe"
        }
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

    // ---------- ADMIN ----------
    fun loginAdmin(usernameAdmin: String, passwordAdmin: String): Boolean {
        if (usernameAdmin.isBlank() || passwordAdmin.isBlank()) {
            mensajeadmin.value = "Credenciales inválidas"
            return false
        }

        val ok = (usernameAdmin == "admin" && passwordAdmin == "admin123")
        mensajeadmin.value = if (ok) "Login exitoso" else "Usuario o contraseña incorrectos"
        return ok
    }

    // ---------- PRODUCTOS (puede usarse en panel admin) ----------
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



    // ---------- ADMIN: Logout ----------
    fun loginAdmin() {
        usuarioActual.value = ""
        mensajeadmin.value = "Sesión cerrada"
    }

    // ---------- ADMIN: Registro ----------
    fun registrarAdmin(usernameAdmin: String, passwordAdmin: String, emailAdmin: String): Boolean {
        if (FakeDatabase.registrarAdmin(usernameAdmin, passwordAdmin, emailAdmin)) {
            mensajeadmin.value = "Registro exitoso"
            return true
        }
        mensajeadmin.value = "El administrador ya existe"
        return false
    }

}




