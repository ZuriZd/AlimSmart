package com.example.alimsmart

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_acitivity)

        // Ocultamos la barra superior para que la pantalla sea completa
        supportActionBar?.hide()

        // Temporizador de 2 segundos (2000 milisegundos)
        Handler(Looper.getMainLooper()).postDelayed({

            // Pasados los 2 segundos, viajamos al Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // Cerramos esta pantalla para que el usuario no pueda regresar a ella
            finish()

        }, 2000)
    }
}