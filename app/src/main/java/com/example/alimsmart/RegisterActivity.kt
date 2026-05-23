package com.example.alimsmart

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Esta capa esta encargada de capturar, sanitizar y validar
 * las credenciales del usuario antes de enviarlas al motor de base de datos (SQLite).
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        // Instanciación del Helper de Base de Datos para abrir el canal de conexión local
        dbHelper = DatabaseHelper(this)

        val etUsuarioReg = findViewById<EditText>(R.id.etNuevoUsuario)
        val etCorreoReg = findViewById<EditText>(R.id.etCorreo)
        val etPasswordReg = findViewById<EditText>(R.id.etPassword)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrar)
        val tvYaTengoCuenta = findViewById<TextView>(R.id.tvVolverLogin)

        btnRegistrarse.setOnClickListener {
            // Extracción y limpieza (trim) de los datos ingresados en el formulario
            val usuario = etUsuarioReg.text.toString().trim()
            val correo = etCorreoReg.text.toString().trim()
            val password = etPasswordReg.text.toString().trim()

            // 1. Capa de Validación (Sanitización básica): Previene inserciones nulas en SQLite
            if (usuario.isEmpty() || correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Ejecución DML: Intenta escribir el registro en la tabla local
            val exito = dbHelper.insertarUsuario(usuario, correo, password)

            if (exito) {
                // Notifica éxito y redirige al flujo de Login
                Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Remueve esta pantalla de la pila para evitar bucles de navegación
            } else {
                Toast.makeText(this, "Error al registrar la cuenta", Toast.LENGTH_SHORT).show()
            }
        }

        // Navegación alternativa
        tvYaTengoCuenta.setOnClickListener {
            finish()
        }
    }
}