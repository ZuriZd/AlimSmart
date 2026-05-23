package com.example.alimsmart

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Esta clase gestiona el mapeo de variables globales de sesión
 */
class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        //Oculta la ActionBar
        supportActionBar?.hide()

        // Vinculación manual de los componentes visuales definidos en el Layout
        val tvVolverPerfil = findViewById<TextView>(R.id.tvVolverPerfil)
        val tvNombrePerfil = findViewById<TextView>(R.id.tvNombrePerfil)
        val tvCorreoPerfil = findViewById<TextView>(R.id.tvCorreoPerfil)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        //Mapea los valores resguardados en el SessionManager.
        // Se aplica el operador Elvis (?:) para asignar textos de auxilio si la memoria llega vacía.
        tvNombrePerfil.text = SessionManager.nombreUsuario ?: "Invitado"
        tvCorreoPerfil.text = SessionManager.correoUsuario ?: "sin_correo@alimsmart.com"

        //Si el usario quiere regresar entonces cierra el ciclo de vida de la Activity actual (destruye la vista)
        // y regresa automáticamente al plano anterior en segundo plano
        tvVolverPerfil.setOnClickListener {
            finish()
        }

        // Evento Click - Cierre Seguro de Sesión
        btnCerrarSesion.setOnClickListener {
            Toast.makeText(this, "Sesión finalizada", Toast.LENGTH_SHORT).show()

            //Elimina los datos del usuario de la sesión actual
            SessionManager.limpiarSesion()

            // Transición y Limpieza del Backstack (Pila de Actividades):
            val intent = Intent(this, LoginActivity::class.java)

            // Estos flags de seguriodad borran por completo el historial de pantallas abiertas.
            // Si el usuario presiona el botón físico de retroceso del teléfono, el sistema operativo
            // cerrará la aplicación en lugar de permitirle reingresar a la tienda sin contraseña.
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish() // Destruye la pantalla de perfil actual
        }
    }
}