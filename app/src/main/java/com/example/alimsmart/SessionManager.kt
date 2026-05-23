package com.example.alimsmart

object SessionManager {
    // Variables para guardar los datos del usuario conectado
    var nombreUsuario: String? = null
    var correoUsuario: String? = null

    // Función para registrar quién entró
    fun guardarSesion(nombre: String, correo: String) {
        nombreUsuario = nombre
        correoUsuario = correo
    }

    // Función para borrar los datos al salir
    fun limpiarSesion() {
        nombreUsuario = null
        correoUsuario = null
    }
}