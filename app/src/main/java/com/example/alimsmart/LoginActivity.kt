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

        btnLogin.setOnClickListener {

            val correo = etEmail.text.toString()
            val password = etPassword.text.toString()

            val usuarioValido =
                dbHelper.validarUsuario(correo, password)

            if (usuarioValido) {

                Toast.makeText(
                    this,
                    "Bienvenido",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "Usuario incorrecto",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        tvRegistro.setOnClickListener {

            val intent =
                Intent(this, RegisterActivity::class.java)

            startActivity(intent)
        }
    }
}