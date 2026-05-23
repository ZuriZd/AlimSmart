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
            val usuario = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // 2. Validamos usando el nombre de usuario
            val usuarioValido = dbHelper.validarUsuario(usuario, password)

            if (usuarioValido) {
                // 3. Usamos la nueva función para ir a buscar el correo real de este usuario
                val correoReal = dbHelper.obtenerCorreoUsuario(usuario)

                // 4. Guardamos los datos reales y perfectos en el SessionManager
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