package com.example.alimsmart

/**
 * Los object Mantiene los datos esenciales del usuario autenticado
 * en la memoria volátil global de la aplicación. Permite que cualquier pantalla (o Activity)
 * acceda al perfil actual de manera inmediata sin saturar el almacenamiento físico (SQLite).
 */
object SessionManager {
    // Variables globales que resguardan la sesión actual del usuario activo
    var nombreUsuario: String? = null
    var correoUsuario: String? = null

    /**
     * Almacena las credenciales validadas del usuario tras un inicio de sesión exitoso.
     * @param nombre Nombre único de identificación de cuenta (Username).
     * @param correo Dirección de correo electrónico asociada al registro.
     */
    fun guardarSesion(nombre: String, correo: String) {
        nombreUsuario = nombre
        correoUsuario = correo
    }

    /**
     * Remueve las referencias de datos de la memoria caché al cerrar sesión.
     * Previene accesos no autorizados al historial si otra persona usa el mismo dispositivo.
     */
    fun limpiarSesion() {
        nombreUsuario = null
        correoUsuario = null
    }
}