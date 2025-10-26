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

    // estado de admin
    val mensajeadmin = mutableStateOf("")

    // usuario
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

    // admin
    fun loginAdmin(usernameAdmin: String, passwordAdmin: String): Boolean {
        //Llamar a la FakeDatabase para la verificación
        val loginExitoso = FakeDatabase.loginAdmin(usernameAdmin, passwordAdmin)

        if (loginExitoso) {
            mensajeadmin.value = "Login exitoso"
            return true
        } else {
            mensajeadmin.value = "Usuario o contraseña incorrectos"
            return false
        }
    }


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



    // Admin logout
    fun loginAdmin() {
        usuarioActual.value = ""
        mensajeadmin.value = "Sesión cerrada"
    }

    // Admin registro
    fun registrarAdmin(usernameAdmin: String, passwordAdmin: String, emailAdmin: String): Boolean {
        if (FakeDatabase.registrarAdmin(usernameAdmin, passwordAdmin, emailAdmin)) {
            mensajeadmin.value = "Registro exitoso"
            return true
        }
        mensajeadmin.value = "El administrador ya existe"
        return false
    }

}




