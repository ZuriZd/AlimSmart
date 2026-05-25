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

    //Funcion que obtiene el nombre de usuario
    fun obtenerUsuario(): String {
        return nombreUsuario ?: "Usuario"
    }

    // Función para borrar los datos al salir
    fun limpiarSesion() {
        nombreUsuario = null
        correoUsuario = null
    }
}