package com.example.apptiendadeportiva_grupo10.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.apptiendadeportiva_grupo10.model.FakeDatabase
import com.example.apptiendadeportiva_grupo10.model.Productos
import com.example.apptiendadeportiva_grupo10.model.Usuario
import kotlin.collections.forEach

class AuthViewModel: ViewModel() {
    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf<String>("")

    var listaProductos = mutableListOf<Productos>()

    fun registrar(id: Int, nombre: String?, password: String?, email: String?){
        val nuevo = Usuario(id, nombre, password, email)
        if (FakeDatabase.registrar(nuevo)) {
            mensaje.value = "Registro exitoso"
        } else {
            mensaje.value = "el usuario ya existe"
        }
    }


    fun login(email: String, password: String): Boolean {
        return if (FakeDatabase.login(email, password)){
            usuarioActual.value = email
            mensaje.value = "inicio de sesion exitoso"
            true
        } else {
            mensaje.value = "credenciales invalidas"
            false
        }
    }

    fun agregarProductos(productos: Productos) {
        listaProductos.add(productos)
        println("Producto agregado")
    }

    fun buscarProducto(idProducto: Int) {
        listaProductos.forEach {
            if (it.idProducto == idProducto) {
                println("Producto encontrado")
            }
        }
    }

    fun eliminarProducto(idProducto: Int){
        listaProductos.forEach {
            if (it.idProducto == idProducto) {
                listaProductos.remove(listaProductos[idProducto])
                println("Producto encontrado")
            }
        }
    }
}