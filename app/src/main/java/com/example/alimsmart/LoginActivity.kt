package com.example.alimsmart

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class LoginActivity : AppCompatActivity() {

    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var tvRegistro: TextView
    lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegistro = findViewById(R.id.tvRegistro)
        dbHelper = DatabaseHelper(this)

        val btnInvitado = findViewById<Button>(R.id.btnInvitado)

        btnInvitado.setOnClickListener {
            // Guardamos la sesión como invitado y saltamos al catálogo
            SessionManager.iniciarSesionInvitado()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            val usuario = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val usuarioValido = dbHelper.validarUsuario(usuario, password)


            if (usuarioValido) {
                val correoReal = dbHelper.obtenerCorreoUsuario(usuario)
                SessionManager.guardarSesion(usuario, correoReal)
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        tvRegistro.setOnClickListener {
            val intent =
                Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
