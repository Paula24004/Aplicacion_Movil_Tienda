package com.example.apptiendadeportiva_grupo10.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import com.example.apptiendadeportiva_grupo10.model.FakeDatabase
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.model.Usuario

class AuthViewModel: ViewModel() {
    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf<String>("")
    var listaProductos = mutableStateListOf<Producto>()


    fun registrar(id: Int, nombre: String?, password: String?, email: String?){
        val nuevo = Usuario(id, nombre, password, email)
        if (FakeDatabase.registrar(nuevo)) {
            mensaje.value = "Registro exitoso"
        } else {
            mensaje.value = "El usuario ya existe"
        }
    }

    fun login(email: String, password: String): Boolean {
        return if (FakeDatabase.login(email, password)){
            usuarioActual.value = email
            mensaje.value = "Inicio de sesión exitoso"
            true
        } else {
            mensaje.value = "Credenciales inválidas"
            false
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
}