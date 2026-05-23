package com.example.alimsmart

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class RegisterActivity : AppCompatActivity() {

    lateinit var etCorreo: EditText
    lateinit var etPassword: EditText
    lateinit var btnRegistrar: Button
    lateinit var tvVolverLogin: TextView
    lateinit var dbHelper: DatabaseHelper
    lateinit var etNuevoUsuario: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etCorreo = findViewById(R.id.etCorreo)
        etPassword = findViewById(R.id.etPassword)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        tvVolverLogin = findViewById(R.id.tvVolverLogin)
        dbHelper = DatabaseHelper(this)
        etNuevoUsuario = findViewById(R.id.etNuevoUsuario)

        btnRegistrar.setOnClickListener {
// 1. Leemos los datos de las casillas (Agrega la línea del usuario)
            val usuario = etNuevoUsuario.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validamos que no dejen campos vacíos
            if (usuario.isEmpty() || correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Pasamos los TRES datos a la base de datos modificada
            val registroExitoso = dbHelper.insertarUsuario(usuario, correo, password)

            if (registroExitoso) {
                Toast.makeText(this, "¡Usuario registrado con éxito!", Toast.LENGTH_SHORT).show()

                // Cerramos esta pantalla para regresar al Login automáticamente
                finish()
            } else {
                Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show()
            }
        }

        tvVolverLogin.setOnClickListener {
            finish()
        }

    }
}