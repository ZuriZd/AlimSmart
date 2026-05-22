package com.example.alimsmart

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class RegisterActivity : AppCompatActivity() {

    lateinit var etCorreo: EditText
    lateinit var etPassword: EditText
    lateinit var btnRegistrar: Button

    lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etCorreo = findViewById(R.id.etCorreo)
        etPassword = findViewById(R.id.etPassword)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        dbHelper = DatabaseHelper(this)

        btnRegistrar.setOnClickListener {

            val correo = etCorreo.text.toString()
            val password = etPassword.text.toString()

            val registrado =
                dbHelper.insertarUsuario(correo, password)

            if (registrado) {

                Toast.makeText(
                    this,
                    "Usuario registrado",
                    Toast.LENGTH_SHORT
                ).show()
                //RETORNO A LA PANTALLA DE LOGIN
                val intent =
                    Intent(this, LoginActivity::class.java)

                startActivity(intent)

                finish()

            } else {

                Toast.makeText(
                    this,
                    "Error al registrar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }
}