package com.example.alimsmart

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil) // <-- Primero se dibuja la vista

        supportActionBar?.hide()

        // 1. Buscamos las casillas del XML por su ID exacto
        val tvVolverPerfil = findViewById<TextView>(R.id.tvVolverPerfil)
        val tvNombrePerfil = findViewById<TextView>(R.id.tvNombrePerfil)
        val tvCorreoPerfil = findViewById<TextView>(R.id.tvCorreoPerfil)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        // 2. ESTA ES LA PARTE CLAVE: Aquí le ordenamos a Kotlin cambiar el texto del XML
        tvNombrePerfil.text = SessionManager.nombreUsuario ?: "Invitado"
        tvCorreoPerfil.text = SessionManager.correoUsuario ?: "sin_correo@alimsmart.com"

        // Botón volver
        tvVolverPerfil.setOnClickListener {
            finish()
        }

        // Botón cerrar sesión
        btnCerrarSesion.setOnClickListener {
            Toast.makeText(this, "Sesión finalizada", Toast.LENGTH_SHORT).show()
            SessionManager.limpiarSesion()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}